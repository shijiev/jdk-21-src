/*
 * Copyright (c) 2014, 2023, Oracle and/or its affiliates. All rights reserved.
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
 */

package sun.jvm.hotspot.gc.shared;

//These definitions should be kept in sync with the definitions in the HotSpot code.

public enum GCCause {
  _java_lang_system_gc ("System.gc()"),
  _full_gc_alot ("FullGCAlot"),
  _scavenge_alot ("ScavengeAlot"),
  _allocation_profiler ("Allocation Profiler"),
  _jvmti_force_gc ("JvmtiEnv ForceGarbageCollection"),
  _gc_locker ("GCLocker Initiated GC"),
  _heap_inspection ("Heap Inspection Initiated GC"),
  _heap_dump ("Heap Dump Initiated GC"),
  _wb_young_gc ("WhiteBox Initiated Young GC"),
  _wb_full_gc ("WhiteBox Initiated Full GC"),

  _no_gc ("No GC"),
  _no_cause_specified ("Unknown GCCause"),
  _allocation_failure ("Allocation Failure"),

  _tenured_generation_full ("Tenured Generation Full"),
  _metadata_GC_threshold ("Metadata GC Threshold"),
  _metadata_GC_clear_soft_refs ("Metadata GC Clear Soft References"),

  _old_generation_expanded_on_last_scavenge ("Old Generation Expanded On Last Scavenge"),
  _old_generation_too_full_to_scavenge ("Old Generation Too Full To Scavenge"),
  _adaptive_size_policy ("Ergonomics"),

  _g1_inc_collection_pause ("G1 Evacuation Pause"),
  _g1_humongous_allocation ("G1 Humongous Allocation"),

  _dcmd_gc_run ("Diagnostic Command"),

  _z_timer ("Timer"),
  _z_warmup ("Warmup"),
  _z_allocation_rate ("Allocation Rate"),
  _z_allocation_stall ("Allocation Stall"),
  _z_proactive ("Proactive"),

  _shenandoah_allocation_failure_evac ("Allocation Failure During Evacuation"),
  _shenandoah_stop_vm ("Stopping VM"),
  _shenandoah_concurrent_gc ("Concurrent GC"),
  _shenandoah_traversal_gc ("Traversal GC"),
  _shenandoah_upgrade_to_full_gc ("Upgrade To Full GC"),

  _last_gc_cause ("ILLEGAL VALUE - last gc cause - ILLEGAL VALUE");

  private final String value;

  GCCause(String val) {
    this.value = val;
  }
  public String value() {
    return value;
  }
}
