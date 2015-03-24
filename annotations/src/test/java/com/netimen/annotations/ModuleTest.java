package com.netimen.annotations;

import com.netimen.annotations.helpers.Module;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ModuleTest {

    @Test
    public void initModuleAndHasModule() {
        final String moduleName = "module";
        assertFalse(Module.moduleExists(moduleName));
        Module.initModule(moduleName);
        assertTrue(Module.moduleExists(moduleName));
    }

    @Test
    public void getInstanceReturnsCorrectObject() {
        assertNull(Module.getInstance(Integer.class));
        Integer object = 5;
        Module.setInsatnce(Integer.class, object);
        assertEquals(object, Module.getInstance(Integer.class));
    }

    @Test
    public void getInstanceReturnsObjectFromCurrentModule() {
        final String moduleName = "module1";
        Module.initModule(moduleName);
        Integer object1 = 5;
        Module.setInsatnce(Integer.class, object1);
        assertEquals(object1, Module.getInstance(Integer.class));

        Module.initModule("module2");
        assertNull(Module.getInstance(Integer.class));
        Integer object2 = 5;
        Module.setInsatnce(Integer.class, object2);
        assertEquals(object2, Module.getInstance(Integer.class));
        assertEquals(object1, Module.getInstance(moduleName, Integer.class));
    }
}