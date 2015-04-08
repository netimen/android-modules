package com.netimen.annotations;


import com.netimen.annotations.helpers.ModuleProvider;

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
        ModuleProvider.clearModules();
    }

    @Test
    public void initModuleAndModuleExists() {
        final String moduleName = "module";
        assertFalse(ModuleProvider.moduleExists(moduleName));
        ModuleProvider.createModule(moduleName);
        assertTrue(ModuleProvider.moduleExists(moduleName));
    }

    @Test
    public void modulesNames() {
        final HashSet<String> modulesNames = new HashSet<>(asList(new String[]{"module1", "module2", "module3"}));
        for (String moduleName: modulesNames)
            ModuleProvider.createModule(moduleName);
        assertEquals(ModuleProvider.modulesNames(), modulesNames);
        ModuleProvider.createModule("aaa");
        assertNotSame(ModuleProvider.modulesNames(), modulesNames);
    }

    @Test
    public void getInstance() {
        ModuleProvider.createModule("module");
        assertNull(ModuleProvider.getInstance(Integer.class));
        final Integer instance = 5;
        ModuleProvider.setInstance(Integer.class, instance);
        assertEquals(instance, ModuleProvider.getInstance(Integer.class));
    }

    @Test
    public void getInstanceModuleName() {
        createInstanceInModule("module1", 5);
        final Integer instance = 4;
        createInstanceInModule("module2", instance);
        assertEquals(instance, ModuleProvider.getInstance(Integer.class)); // when retrieving without modulename, should return last module's instance
    }

    void createInstanceInModule(String moduleName, Integer instance) {
        ModuleProvider.createModule(moduleName);
        assertNull(ModuleProvider.getInstance(Integer.class));
        ModuleProvider.setInstance(Integer.class, instance);
        assertEquals(instance, ModuleProvider.getInstance(moduleName, Integer.class));
    }
}