/*
 * Copyright (c) 1998, 2023, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package jdk.javadoc.internal.doclets.toolkit.util;

import java.util.*;
import java.util.stream.Collectors;

import javax.lang.model.element.Element;
import javax.lang.model.element.ModuleElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

import com.sun.source.doctree.DocTree;
import jdk.javadoc.internal.doclets.toolkit.BaseConfiguration;
import jdk.javadoc.internal.doclets.toolkit.Messages;

import static jdk.javadoc.internal.doclets.toolkit.util.VisibleMemberTable.Kind.*;

/**
 * An alphabetical index of elements, search tags, and other items.
 * Two tables are maintained:
 * one is indexed by the first character of each items name;
 * the other is index by the item's category, indicating the JavaScript
 * file in which the item should be written.
 */
public class IndexBuilder {

    /**
     * Sets of items keyed by the first character of the names (labels)
     * of the items in those sets.
     */
    private final Map<Character, SortedSet<IndexItem>> itemsByFirstChar;

    /**
     * Sets of items keyed by the {@link IndexItem.Category category}
     * of the items in those sets.
     */
    private final Map<IndexItem.Category, SortedSet<IndexItem>> itemsByCategory;

    /**
     * Don't generate deprecated information if true.
     */
    private final boolean noDeprecated;

    /**
     * Build this index only for classes?
     */
    protected final boolean classesOnly;

    private final BaseConfiguration configuration;
    private final Utils utils;

    /**
     * The comparator used for the sets in {@code itemsByFirstChar}.
     */
    private final Comparator<IndexItem> mainComparator;

    /**
     * Creates a new {@code IndexBuilder}.
     *
     * @param configuration the current configuration of the doclet
     * @param noDeprecated  true if -nodeprecated option is used,
     *                      false otherwise
     */
    public IndexBuilder(BaseConfiguration configuration,
                        boolean noDeprecated)
    {
        this(configuration, noDeprecated, false);
    }

    /**
     * Creates a new {@code IndexBuilder}.
     *
     * @param configuration the current configuration of the doclet
     * @param noDeprecated  true if -nodeprecated option is used,
     *                      false otherwise
     * @param classesOnly   include only classes in index
     */
    public IndexBuilder(BaseConfiguration configuration,
                        boolean noDeprecated,
                        boolean classesOnly)
    {
        this.configuration = configuration;
        this.utils = configuration.utils;

        Messages messages = configuration.getMessages();
        if (classesOnly) {
            messages.notice("doclet.Building_Index_For_All_Classes");
        } else {
            messages.notice("doclet.Building_Index");
        }

        this.noDeprecated = noDeprecated;
        this.classesOnly = classesOnly;

        itemsByFirstChar = new TreeMap<>();
        itemsByCategory = new EnumMap<>(IndexItem.Category.class);

        mainComparator = classesOnly ? makeClassComparator() : makeIndexComparator();
    }

    /**
     * Adds all the selected modules, packages, types and their members to the index,
     * or just the type elements if {@code classesOnly} is {@code true}.
     */
    public void addElements()  {
        Set<TypeElement> classes = configuration.getIncludedTypeElements();
        indexTypeElements(classes);
        if (classesOnly) {
            return;
        }
        Set<PackageElement> packages = configuration.getSpecifiedPackageElements();
        if (packages.isEmpty()) {
            packages = classes
                    .stream()
                    .map(utils::containingPackage)
                    .filter(_package -> _package != null && !_package.isUnnamed())
                    .collect(Collectors.toSet());
        }
        packages.forEach(this::indexPackage);
        classes.stream()
               .filter(this::shouldIndex)
               .forEach(this::indexMembers);

        if (configuration.showModules) {
            indexModules();
        }
    }

    /**
     * Adds an individual item to the two collections of items.
     *
     * @param item the item to add
     */
    public void add(IndexItem item) {
        Objects.requireNonNull(item);

        if (item.isElementItem() || item.isTagItem()) {
            // don't put summary-page items in the A-Z index:
            // they are listed separately, at the top of the index page
            itemsByFirstChar.computeIfAbsent(keyCharacter(item.getLabel()),
                    c -> new TreeSet<>(mainComparator))
                    .add(item);
        }

        itemsByCategory.computeIfAbsent(item.getCategory(),
                    c -> new TreeSet<>(c == IndexItem.Category.TYPES
                            ? makeTypeSearchIndexComparator()
                            : makeGenericSearchIndexComparator()))
                .add(item);
    }

    /**
     * Returns a sorted list of items whose names start with the
     * provided character.
     *
     * @param key index key
     * @return list of items keyed by the provided character
     */
    public SortedSet<IndexItem> getItems(Character key) {
        return itemsByFirstChar.get(key);
    }

    /**
     * Returns a sorted list of the first characters of the labels of index items.
     */
    public List<Character> getFirstCharacters() {
        return new ArrayList<>(itemsByFirstChar.keySet());
    }

    /**
     * Returns a sorted list of items in a given category.
     *
     * @param cat the category
     * @return list of items keyed by the provided character
     */
    public SortedSet<IndexItem> getItems(IndexItem.Category cat) {
        Objects.requireNonNull(cat);
        return itemsByCategory.getOrDefault(cat, Collections.emptySortedSet());
    }

    /**
     * Returns a sorted list of items with a given kind of doc tree.
     *
     * @param kind the kind
     * @return list of items keyed by the provided character
     */
    public SortedSet<IndexItem> getItems(DocTree.Kind kind) {
        Objects.requireNonNull(kind);
        return itemsByCategory.getOrDefault(IndexItem.Category.TAGS, Collections.emptySortedSet()).stream()
                .filter(i -> i.isKind(kind))
                .collect(Collectors.toCollection(() -> new TreeSet<>(mainComparator)));
    }

    /**
     * Indexes all the members (fields, methods, constructors, etc.) of the
     * provided type element.
     *
     * @param te TypeElement whose members are to be indexed
     */
    private void indexMembers(TypeElement te) {
        VisibleMemberTable vmt = configuration.getVisibleMemberTable(te);
        indexMembers(te, vmt.getVisibleMembers(FIELDS));
        indexMembers(te, vmt.getVisibleMembers(ANNOTATION_TYPE_MEMBER_OPTIONAL));
        indexMembers(te, vmt.getVisibleMembers(ANNOTATION_TYPE_MEMBER_REQUIRED));
        indexMembers(te, vmt.getVisibleMembers(METHODS));
        indexMembers(te, vmt.getVisibleMembers(CONSTRUCTORS));
        indexMembers(te, vmt.getVisibleMembers(ENUM_CONSTANTS));
    }

    /**
     * Indexes the provided elements.
     *
     * @param members a collection of elements
     */
    private void indexMembers(TypeElement typeElement, Iterable<? extends Element> members) {
        for (Element member : members) {
            if (shouldIndex(member)) {
                add(IndexItem.of(typeElement, member, utils));
            }
        }
    }

    /**
     * Index the given type elements.
     *
     * @param elements type elements
     */
    private void indexTypeElements(Iterable<TypeElement> elements) {
        for (TypeElement typeElement : elements) {
            if (shouldIndex(typeElement)) {
                add(IndexItem.of(typeElement, utils));
            }
        }
    }

    /**
     * Indexes all the modules.
     */
    private void indexModules() {
        for (ModuleElement m : configuration.modules) {
            add(IndexItem.of(m, utils));
        }
    }

    /**
     * Index the given package element.
     *
     * @param packageElement the package element
     */
    private void indexPackage(PackageElement packageElement) {
        if (shouldIndex(packageElement)) {
            add(IndexItem.of(packageElement, utils));
        }
    }

    /**
     * Should this element be added to the index?
     */
    private boolean shouldIndex(Element element) {
        if (utils.hasHiddenTag(element)) {
            return false;
        }

        if (utils.isPackage(element)) {
            // Do not add to index map if -nodeprecated option is set and the
            // package is marked as deprecated.
            return !(noDeprecated && utils.isDeprecated(element));
        } else {
            // Do not add to index map if -nodeprecated option is set and if the
            // element is marked as deprecated or the containing package is marked as
            // deprecated.
            return !(noDeprecated &&
                    (utils.isDeprecated(element) ||
                    utils.isDeprecated(utils.containingPackage(element))));
        }
    }

    private static Character keyCharacter(String s) {
        // Use first valid java identifier start character as key,
        // or '*' for strings that do not contain one.
        for (int i = 0; i < s.length(); i++) {
            if (Character.isJavaIdentifierStart(s.charAt(i))) {
                return Character.toUpperCase(s.charAt(i));
            }
        }
        return '*';
    }

    /**
     * Returns a comparator for the all-classes list.
     * @return a comparator for class element items
     */
    private Comparator<IndexItem> makeClassComparator() {
        return Comparator.comparing(IndexItem::getElement, utils.comparators.makeAllClassesComparator());
    }

    /**
     * Returns a comparator for the {@code IndexItem}s in the index page.
     * This is a composite comparator that must be able to compare all kinds of items:
     * for element items, tag items, and others.
     *
     * @return a comparator for index page items
     */
    private Comparator<IndexItem> makeIndexComparator() {
        // We create comparators specific to element and search tag items, and a
        // base comparator used to compare between the two kinds of items.
        // In order to produce consistent results, it is important that the base comparator
        // uses the same primary sort keys as both the element and search tag comparators
        // (see JDK-8311264).
        Comparator<Element> elementComparator = utils.comparators.makeIndexElementComparator();
        Comparator<IndexItem> baseComparator =
                (ii1, ii2) -> utils.compareStrings(getIndexItemKey(ii1), getIndexItemKey(ii2));
        Comparator<IndexItem> searchTagComparator =
                baseComparator
                        .thenComparing(IndexItem::getHolder)
                        .thenComparing(IndexItem::getDescription)
                        .thenComparing(IndexItem::getUrl);

        return (ii1, ii2) -> {
            // If both are element items, compare the elements
            if (ii1.isElementItem() && ii2.isElementItem()) {
                int d = elementComparator.compare(ii1.getElement(), ii2.getElement());
                if (d == 0) {
                    /*
                     * Members inherited from classes with package access are
                     * documented as though they were declared in the inheriting
                     * subclass (see JDK-4780441).
                     */
                    Element subclass1 = ii1.getContainingTypeElement();
                    Element subclass2 = ii2.getContainingTypeElement();
                    if (subclass1 != null && subclass2 != null) {
                        d = elementComparator.compare(subclass1, subclass2);
                    }
                }
                return d;
            }

            // If one is an element item, compare item keys; if equal, put element item last
            if (ii1.isElementItem() || ii2.isElementItem()) {
                int d = baseComparator.compare(ii1, ii2);
                return d != 0 ? d : ii1.isElementItem() ? 1 : -1;
            }

            // Otherwise, compare labels and other fields of the items
            return searchTagComparator.compare(ii1, ii2);
        };
    }

    private String getIndexItemKey(IndexItem ii) {
        // For element items return the key used by the element comparator;
        // for search tag items return the item's label.
        return ii.isElementItem()
                ? utils.comparators.getIndexElementKey(ii.getElement())
                : ii.getLabel();
    }

    /**
     * Returns a Comparator for IndexItems in the types category of the search index.
     * Items are compared by short name, falling back to the main comparator if names are equal.
     *
     * @return a Comparator
     */
    public Comparator<IndexItem> makeTypeSearchIndexComparator() {
        Comparator<IndexItem> simpleNameComparator =
                (ii1, ii2) -> utils.compareStrings(ii1.getSimpleName(), ii2.getSimpleName());
        return simpleNameComparator.thenComparing(mainComparator);
    }

    /**
     * Returns a Comparator for IndexItems in the modules, packages, members, and search tags
     * categories of the search index.
     * Items are compared by label, falling back to the main comparator if names are equal.
     *
     * @return a Comparator
     */
    public Comparator<IndexItem> makeGenericSearchIndexComparator() {
        Comparator<IndexItem> labelComparator =
                (ii1, ii2) -> utils.compareStrings(ii1.getLabel(), ii2.getLabel());
        return labelComparator.thenComparing(mainComparator);
    }
}
