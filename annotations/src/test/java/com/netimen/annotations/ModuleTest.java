package com.netimen.annotations;

import com.netimen.annotations.helpers.Module;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ModuleTest {

    @Before
    public void setUp() {
        Module.clearModules();
    }

    @Test
    public void initModuleAndHasModule() {
        final String moduleName = "module";
        assertFalse(Module.moduleExists(moduleName));
        Module.initModule(moduleName);
        assertTrue("  aaaa + " + Module.moduleExists(moduleName), Module.moduleExists(moduleName));
    }

    @Test
    public void getInstanceReturnsCorrectObject() {
        assertNull(Module.getInstance(Integer.class));
        Integer object = 5;
        Module.setInstance(Integer.class, object);
        assertEquals(object, Module.getInstance(Integer.class));
    }

    @Test
    public void getInstanceReturnsObjectFromCurrentModule() {
        final String moduleName = "module";
        Module.initModule(moduleName);
        Integer object1 = 5;
        Module.setInstance(Integer.class, object1);
        assertEquals(object1, Module.getInstance(Integer.class));

        Module.initModule("module2");
        assertNull(Module.getInstance(Integer.class));
        Integer object2 = 5;
        Module.setInstance(Integer.class, object2);
        assertEquals(object2, Module.getInstance(Integer.class));
        assertEquals(object1, Module.getInstance(moduleName, Integer.class));
    }

    @Test
    public void initModuleWithSameNameUsesCurrentModule() {
        final String moduleName = "module";
        Module.initModule(moduleName);
        Integer object1 = 5;
        Module.setInstance(Integer.class, object1);
        assertEquals(object1, Module.getInstance(Integer.class));

        Module.initModule(moduleName);
        assertEquals(object1, Module.getInstance(Integer.class));
    }
    @Test
    public void initModuleRecreatesModuleWithSameName() {
        final String moduleName = "module";
        Module.initModule(moduleName);
        Integer object = 5;
        Module.setInstance(Integer.class, object);
        assertEquals(object, Module.getInstance(Integer.class));

        Module.initModule("module2");
        Module.initModule(moduleName);
        assertNull(Module.getInstance(Integer.class));

    }
}