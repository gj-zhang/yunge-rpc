package com.rancho.yunge.registry;

import com.rancho.yunge.lifecycle.LifeCycle;

import java.util.Set;

public interface Registry extends LifeCycle {

    boolean registry(Set<String> keys, String value);


}
