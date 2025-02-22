// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
// This file is copied from
// https://github.com/trinodb/trino/blob/master/core/trino-main/src/main/java/io/trino/execution/scheduler/ResettableRandomizedIterator.java
// and modified by Doris

package org.apache.doris.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;

public class ResettableRandomizedIterator<T>
        implements Iterator<T> {
    private final List<T> list;
    private int position;

    public ResettableRandomizedIterator(Collection<T> elements) {
        this.list = new ArrayList<>(elements);
    }

    public void reset() {
        position = 0;
    }

    @Override
    public boolean hasNext() {
        return position < list.size();
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        int position = ThreadLocalRandom.current().nextInt(this.position, list.size());

        T result = list.set(position, list.get(this.position));
        list.set(this.position, result);
        this.position++;

        return result;
    }
}

