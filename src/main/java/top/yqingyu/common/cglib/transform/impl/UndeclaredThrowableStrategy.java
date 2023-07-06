/*
 * Copyright 2003 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package top.yqingyu.common.cglib.transform.impl;

import top.yqingyu.common.cglib.core.*;
import top.yqingyu.common.cglib.transform.MethodFilter;
import top.yqingyu.common.cglib.transform.MethodFilterTransformer;
import top.yqingyu.common.cglib.transform.TransformingClassGenerator;

/**
 * A {@link GeneratorStrategy} suitable for use with {@link top.yqingyu.common.cglib.Enhancer} which
 * causes all undeclared exceptions thrown from within a proxied method to be wrapped
 * in an alternative exception of your choice.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class UndeclaredThrowableStrategy extends DefaultGeneratorStrategy {

    private Class wrapper;

	/**
     * Create a new instance of this strategy.
     * @param wrapper a class which extends either directly or
     * indirectly from <code>Throwable</code> and which has at least one
     * constructor that takes a single argument of type
     * <code>Throwable</code>, for example
     * <code>java.lang.reflect.UndeclaredThrowableException.class</code>
     */
    public UndeclaredThrowableStrategy(Class wrapper) {
       this.wrapper = wrapper;
    }

    private static final MethodFilter TRANSFORM_FILTER = (access, name, desc, signature, exceptions) -> !TypeUtils.isPrivate(access) && name.indexOf('$') < 0;

    @Override
	protected ClassGenerator transform(ClassGenerator cg) throws Exception {
    	 ClassTransformer   tr = new UndeclaredThrowableTransformer(wrapper);
         tr = new MethodFilterTransformer(TRANSFORM_FILTER, tr);
        return new TransformingClassGenerator(cg, tr);
    }
}

