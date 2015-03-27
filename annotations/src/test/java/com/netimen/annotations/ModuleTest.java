package com.netimen.annotations;


import com.netimen.annotations.helpers.ModuleInstancesHolder;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ModuleTest {

    @Before
    public void setUp() {
        ModuleInstancesHolder.clearModules();
    }

    @Test
    public void initModuleAndHasModule() {
        final String moduleName = "module";
        assertFalse(ModuleInstancesHolder.moduleExists(moduleName));
        @SuppressWarnings("UnusedDeclaration") final ModuleInstancesHolder module = ModuleInstancesHolder.initModule(moduleName);
        assertTrue(ModuleInstancesHolder.moduleExists(moduleName));
    }

    @Test
    public void initModulePreservesObjectsInExistingModuleWithSameName() {
        assertEquals(testInitModule(false), ModuleInstancesHolder.getInstance(Integer.class));
    }

    @Test
    public void initModuleClearsExistingModuleWithSameName() {
        testInitModule(true);
        assertNull(ModuleInstancesHolder.getInstance(Integer.class));
    }

    private Integer testInitModule(boolean reset) {
        final String moduleName = "module";
        final ModuleInstancesHolder module = ModuleInstancesHolder.initModule(moduleName);
        Integer object = 5;
        ModuleInstancesHolder.setInstance(Integer.class, object);
        assertEquals(object, ModuleInstancesHolder.getInstance(Integer.class));

        ModuleInstancesHolder.initModule("module2");
        assertEquals(module, ModuleInstancesHolder.initModule(moduleName, reset));
        return object;
    }
//    @Test
//    public void getInstanceReturnsNullAfterObjectDeath() {
//        ModuleInstancesHolder.setInstance(Integer.class, 5);
//        assertNull(ModuleInstancesHolder.getInstance(Integer.class));
//    }

    @Test
    public void getInstanceReturnsCorrectObject() {
        assertNull(ModuleInstancesHolder.getInstance(Integer.class));
        Integer object = 5;
        ModuleInstancesHolder.setInstance(Integer.class, object);
        assertEquals(object, ModuleInstancesHolder.getInstance(Integer.class));
    }

    @Test
    public void getInstanceReturnsObjectFromCurrentModule() {
        final String moduleName = "module";
        ModuleInstancesHolder.initModule(moduleName);
        Integer object1 = 5;
        ModuleInstancesHolder.setInstance(Integer.class, object1);
        assertEquals(object1, ModuleInstancesHolder.getInstance(Integer.class));

        ModuleInstancesHolder.initModule("module2");
        assertNull(ModuleInstancesHolder.getInstance(Integer.class));
        Integer object2 = 5;
        ModuleInstancesHolder.setInstance(Integer.class, object2);
        assertEquals(object2, ModuleInstancesHolder.getInstance(Integer.class));
        assertEquals(object1, ModuleInstancesHolder.getInstance(moduleName, Integer.class));
    }

    @Test
    public void initModuleWithSameNameUsesCurrentModule() {
        final String moduleName = "module";
        ModuleInstancesHolder.initModule(moduleName);
        Integer object1 = 5;
        ModuleInstancesHolder.setInstance(Integer.class, object1);
        assertEquals(object1, ModuleInstancesHolder.getInstance(Integer.class));

        ModuleInstancesHolder.initModule(moduleName);
        assertEquals(object1, ModuleInstancesHolder.getInstance(Integer.class));
    }

}