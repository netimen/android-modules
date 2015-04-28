package com.netimen.androidmodules;


import com.netimen.androidmodules.helpers.ModuleObjectsShare;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

public class ModuleTest {

    @Before
    public void setUp() {
        ModuleObjectsShare.clearModules();
    }

    @Test
    public void initModuleAndModuleExists() {
        final String moduleName = "module";
        assertFalse(ModuleObjectsShare.moduleExists(moduleName));
        ModuleObjectsShare.createModule(moduleName);
        assertTrue(ModuleObjectsShare.moduleExists(moduleName));
    }

    @Test
    public void modulesNames() {
        final HashSet<String> modulesNames = new HashSet<>(asList(new String[]{"module1", "module2", "module3"}));
        for (String moduleName: modulesNames)
            ModuleObjectsShare.createModule(moduleName);
        assertEquals(ModuleObjectsShare.modulesNames(), modulesNames);
        ModuleObjectsShare.createModule("aaa");
        assertNotSame(ModuleObjectsShare.modulesNames(), modulesNames);
    }

    @Test
    public void getInstance() {
        ModuleObjectsShare.createModule("module");
        assertNull(ModuleObjectsShare.getInstance(Integer.class));
        final Integer instance = 5;
        ModuleObjectsShare.setInstance(Integer.class, instance);
        assertEquals(instance, ModuleObjectsShare.getInstance(Integer.class));
    }

    @Test
    public void getInstanceModuleName() {
        createInstanceInModule("module1", 5);
        final Integer instance = 4;
        createInstanceInModule("module2", instance);
        assertEquals(instance, ModuleObjectsShare.getInstance(Integer.class)); // when retrieving without modulename, should return last module's instance
    }

    void createInstanceInModule(String moduleName, Integer instance) {
        ModuleObjectsShare.createModule(moduleName);
        assertNull(ModuleObjectsShare.getInstance(Integer.class));
        ModuleObjectsShare.setInstance(Integer.class, instance);
        assertEquals(instance, ModuleObjectsShare.getInstance(moduleName, Integer.class));
    }
}