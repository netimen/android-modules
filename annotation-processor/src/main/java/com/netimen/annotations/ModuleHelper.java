/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   24.03.15
 */
package com.netimen.annotations;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

import org.androidannotations.holder.EComponentHolder;

import static com.sun.codemodel.JExpr._new;
import static com.sun.codemodel.JExpr._null;

public class ModuleHelper {
    static public void returnNewInstance(EComponentHolder holder, JMethod method, JDefinedClass instanceCls, JInvocation newInstance) {
        method.body()._return(getInstance(holder, method, instanceCls, newInstance));
    }

     static private JVar getInstance(EComponentHolder holder, JMethod method, JDefinedClass instanceCls, JInvocation newInstance) {
        final JVar instance = method.body().decl(JMod.NONE, instanceCls, "instance", moduleGetInstance(holder, instanceCls));
        method.body()._if(instance.eq(_null()))._then().assign(instance, newInstance);
        return instance;
    }

    public static JInvocation moduleGetInstance(EComponentHolder holder, JClass instanceCls) {
        return holder.refClass(com.netimen.annotations.helpers.Module.class).staticInvoke("getInstance").arg(instanceCls.dotclass());
    }

    public static JInvocation moduleSetInstance(EComponentHolder holder, JClass instanceCls) {
        return moduleSetInstance(holder, instanceCls, _new(instanceCls));
    }
    public static JInvocation moduleSetInstance(EComponentHolder holder, JClass instanceCls, JExpression newInstance) {
        return holder.refClass(com.netimen.annotations.helpers.Module.class).staticInvoke("setInstance").arg(instanceCls.dotclass()).arg(newInstance);
    }
}
