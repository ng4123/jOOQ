/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.test;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.jooq.Clause.CONDITION;
import static org.jooq.Clause.CONDITION_AND;
import static org.jooq.Clause.CONDITION_BETWEEN;
import static org.jooq.Clause.CONDITION_BETWEEN_SYMMETRIC;
import static org.jooq.Clause.CONDITION_COMPARISON;
import static org.jooq.Clause.CONDITION_EXISTS;
import static org.jooq.Clause.CONDITION_IN;
import static org.jooq.Clause.CONDITION_IS_NOT_NULL;
import static org.jooq.Clause.CONDITION_IS_NULL;
import static org.jooq.Clause.CONDITION_NOT;
import static org.jooq.Clause.CONDITION_NOT_BETWEEN;
import static org.jooq.Clause.CONDITION_NOT_BETWEEN_SYMMETRIC;
import static org.jooq.Clause.CONDITION_NOT_EXISTS;
import static org.jooq.Clause.CONDITION_NOT_IN;
import static org.jooq.Clause.CONDITION_OR;
import static org.jooq.Clause.FIELD;
import static org.jooq.Clause.FIELD_ALIAS;
import static org.jooq.Clause.FIELD_REFERENCE;
import static org.jooq.Clause.FIELD_ROW;
import static org.jooq.Clause.FIELD_VALUE;
import static org.jooq.Clause.INSERT;
import static org.jooq.Clause.INSERT_INSERT_INTO;
import static org.jooq.Clause.INSERT_ON_DUPLICATE_KEY_UPDATE;
import static org.jooq.Clause.INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT;
import static org.jooq.Clause.INSERT_RETURNING;
import static org.jooq.Clause.INSERT_SELECT;
import static org.jooq.Clause.INSERT_VALUES;
import static org.jooq.Clause.SELECT;
import static org.jooq.Clause.SELECT_CONNECT_BY;
import static org.jooq.Clause.SELECT_FROM;
import static org.jooq.Clause.SELECT_GROUP_BY;
import static org.jooq.Clause.SELECT_HAVING;
import static org.jooq.Clause.SELECT_ORDER_BY;
import static org.jooq.Clause.SELECT_SELECT;
import static org.jooq.Clause.SELECT_START_WITH;
import static org.jooq.Clause.SELECT_UNION_ALL;
import static org.jooq.Clause.SELECT_WHERE;
import static org.jooq.Clause.TABLE;
import static org.jooq.Clause.TABLE_ALIAS;
import static org.jooq.Clause.TABLE_REFERENCE;
import static org.jooq.Clause.TABLE_VALUES;
import static org.jooq.Clause.UPDATE;
import static org.jooq.Clause.UPDATE_RETURNING;
import static org.jooq.Clause.UPDATE_SET;
import static org.jooq.Clause.UPDATE_SET_ASSIGNMENT;
import static org.jooq.Clause.UPDATE_UPDATE;
import static org.jooq.Clause.UPDATE_WHERE;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.DSL.values;
import static org.jooq.impl.DefaultVisitListenerProvider.providers;
import static org.jooq.test.data.Table1.FIELD_DATE1;
import static org.jooq.test.data.Table1.FIELD_ID1;
import static org.jooq.test.data.Table1.FIELD_NAME1;
import static org.jooq.test.data.Table1.TABLE1;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Clause;
import org.jooq.DSLContext;
import org.jooq.VisitContext;
import org.jooq.VisitListener;
import org.jooq.impl.DSL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Some common tests related to {@link VisitContext}
 *
 * @author Lukas Eder
 */
@SuppressWarnings("unchecked")
public class VisitContextTest extends AbstractTest {

    private DSLContext ctx;
    private SimpleListener listener;

    @Before
    public void setup() {
        listener = new SimpleListener();
        ctx = DSL.using(
            create.configuration().derive(providers(listener)));
    }

    @After
    public void teardown() {
        ctx = null;
        listener = null;
    }

    @Test
    public void test_table_VALUES() {

        // Postgres supports VALUES table constructors
        ctx.configuration().set(POSTGRES);
        ctx.renderContext().declareTables(true).render(values(row(1, "value"), row(2, "value")));

        assertEvents(asList(
            asList(TABLE),
            asList(TABLE, TABLE_ALIAS),
            asList(TABLE, TABLE_ALIAS, TABLE),
            asList(TABLE, TABLE_ALIAS, TABLE, TABLE_VALUES),
            asList(TABLE, TABLE_ALIAS, TABLE, TABLE_VALUES, FIELD_ROW),
            asList(TABLE, TABLE_ALIAS, TABLE, TABLE_VALUES, FIELD_ROW, FIELD),
            asList(TABLE, TABLE_ALIAS, TABLE, TABLE_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(TABLE, TABLE_ALIAS, TABLE, TABLE_VALUES, FIELD_ROW, FIELD),
            asList(TABLE, TABLE_ALIAS, TABLE, TABLE_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(TABLE, TABLE_ALIAS, TABLE, TABLE_VALUES, FIELD_ROW),
            asList(TABLE, TABLE_ALIAS, TABLE, TABLE_VALUES, FIELD_ROW, FIELD),
            asList(TABLE, TABLE_ALIAS, TABLE, TABLE_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(TABLE, TABLE_ALIAS, TABLE, TABLE_VALUES, FIELD_ROW, FIELD),
            asList(TABLE, TABLE_ALIAS, TABLE, TABLE_VALUES, FIELD_ROW, FIELD, FIELD_VALUE)
        ));
    }

    @Test
    public void test_tableAliasReference() {
        ctx.render(TABLE1.as("x"));

        assertEvents(asList(
            asList(TABLE),
            asList(TABLE, TABLE_REFERENCE)
        ));
    }

    @Test
    public void test_tableAliasDeclaration() {
        ctx.renderContext().declareTables(true).render(TABLE1.as("x"));

        assertEvents(asList(
            asList(TABLE),
            asList(TABLE, TABLE_ALIAS),
            asList(TABLE, TABLE_ALIAS, TABLE),
            asList(TABLE, TABLE_ALIAS, TABLE, TABLE_REFERENCE)
        ));
    }

    @Test
    public void test_fieldAliasReference() {
        ctx.render(FIELD_ID1.as("x"));

        assertEvents(asList(
            asList(FIELD),
            asList(FIELD, FIELD_REFERENCE)
        ));
    }

    @Test
    public void test_fieldAliasDeclaration() {
        ctx.renderContext().declareFields(true).render(FIELD_ID1.as("x"));

        assertEvents(asList(
            asList(FIELD),
            asList(FIELD, FIELD_ALIAS),
            asList(FIELD, FIELD_ALIAS, FIELD),
            asList(FIELD, FIELD_ALIAS, FIELD, FIELD_REFERENCE)
        ));
    }

    @Test
    public void test_INSERT_VALUES_simple() {
        ctx.insertInto(TABLE1)
           .values(1, "value", null)
           .getSQL();

        assertEvents(asList(
            asList(INSERT),
            asList(INSERT, INSERT_INSERT_INTO),
            asList(INSERT, INSERT_INSERT_INTO, TABLE),
            asList(INSERT, INSERT_INSERT_INTO, TABLE, TABLE_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_VALUES),
            asList(INSERT, INSERT_VALUES, FIELD_ROW),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE),
            asList(INSERT, INSERT_RETURNING)
        ));
    }

    @Test
    public void test_INSERT_VALUES_RETURNING() {

        // Postgres is the only dialect to actually have a RETURNING clause.
        ctx.configuration().set(POSTGRES);
        ctx.insertInto(TABLE1)
           .values(1, "value", null)
           .returning(FIELD_ID1, FIELD_NAME1)
           .getSQL();

        assertEvents(asList(
            asList(INSERT),
            asList(INSERT, INSERT_INSERT_INTO),
            asList(INSERT, INSERT_INSERT_INTO, TABLE),
            asList(INSERT, INSERT_INSERT_INTO, TABLE, TABLE_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_VALUES),
            asList(INSERT, INSERT_VALUES, FIELD_ROW),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE),
            asList(INSERT, INSERT_RETURNING),
            asList(INSERT, INSERT_RETURNING, FIELD),
            asList(INSERT, INSERT_RETURNING, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_RETURNING, FIELD),
            asList(INSERT, INSERT_RETURNING, FIELD, FIELD_REFERENCE)
        ));
    }

    @Test
    public void test_INSERT_VALUES_ON_DUPLICATE_KEY() {

        // Use MySQL to actually render ON DUPLICATE KEY UPDATE
        ctx.configuration().set(MYSQL);
        ctx.insertInto(TABLE1)
           .values(1, "value", null)
           .onDuplicateKeyUpdate()
           .set(FIELD_NAME1, "value")
           .set(FIELD_DATE1, FIELD_DATE1)
           .getSQL();

        assertEvents(asList(
            asList(INSERT),
            asList(INSERT, INSERT_INSERT_INTO),
            asList(INSERT, INSERT_INSERT_INTO, TABLE),
            asList(INSERT, INSERT_INSERT_INTO, TABLE, TABLE_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_VALUES),
            asList(INSERT, INSERT_VALUES, FIELD_ROW),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE),
            asList(INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE, INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT),
            asList(INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE, INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT, FIELD),
            asList(INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE, INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE, INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT, FIELD),
            asList(INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE, INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE, INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT),
            asList(INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE, INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT, FIELD),
            asList(INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE, INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE, INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT, FIELD),
            asList(INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE, INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_RETURNING)
        ));
    }

    @Test
    public void test_INSERT_VALUES_multiple() {

        // Postgres has a native implementation for multi-value inserts
        ctx.configuration().set(POSTGRES);
        ctx.insertInto(TABLE1)
           .values(1, "value", null)
           .values(2, "value", null)
           .getSQL();

        assertEvents(asList(
            asList(INSERT),
            asList(INSERT, INSERT_INSERT_INTO),
            asList(INSERT, INSERT_INSERT_INTO, TABLE),
            asList(INSERT, INSERT_INSERT_INTO, TABLE, TABLE_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_VALUES),
            asList(INSERT, INSERT_VALUES, FIELD_ROW),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_VALUES, FIELD_ROW),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE),
            asList(INSERT, INSERT_RETURNING)
        ));
    }

    @Test
    public void test_INSERT_VALUES_multiple_emulated() {

        // Oracle emulates multi-record inserts through INSERT .. SELECT
        ctx.insertInto(TABLE1)
           .values(1, "value", null)
           .values(2, "value", null)
           .getSQL();

        assertEvents(asList(
            asList(INSERT),
            asList(INSERT, INSERT_INSERT_INTO),
            asList(INSERT, INSERT_INSERT_INTO, TABLE),
            asList(INSERT, INSERT_INSERT_INTO, TABLE, TABLE_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_SELECT),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_SELECT),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_SELECT, FIELD),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_SELECT, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_SELECT, FIELD),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_SELECT, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_SELECT, FIELD),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_SELECT, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_FROM),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_FROM, TABLE),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_WHERE),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_START_WITH),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_CONNECT_BY),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_GROUP_BY),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_HAVING),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_ORDER_BY),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_SELECT),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_SELECT, FIELD),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_SELECT, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_SELECT, FIELD),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_SELECT, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_SELECT, FIELD),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_SELECT, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_FROM),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_FROM, TABLE),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_WHERE),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_START_WITH),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_CONNECT_BY),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_GROUP_BY),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_HAVING),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_ORDER_BY),
            asList(INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE),
            asList(INSERT, INSERT_RETURNING)
        ));
    }

    @Test
    public void test_INSERT_SELECT() {
        ctx.insertInto(TABLE1)
           .select(select(val(1), val("value"), val(null)))
           .getSQL();

        assertEvents(asList(
            asList(INSERT),
            asList(INSERT, INSERT_INSERT_INTO),
            asList(INSERT, INSERT_INSERT_INTO, TABLE),
            asList(INSERT, INSERT_INSERT_INTO, TABLE, TABLE_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_SELECT),
            asList(INSERT, INSERT_SELECT, SELECT),
            asList(INSERT, INSERT_SELECT, SELECT, SELECT_SELECT),
            asList(INSERT, INSERT_SELECT, SELECT, SELECT_SELECT, FIELD),
            asList(INSERT, INSERT_SELECT, SELECT, SELECT_SELECT, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_SELECT, SELECT, SELECT_SELECT, FIELD),
            asList(INSERT, INSERT_SELECT, SELECT, SELECT_SELECT, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_SELECT, SELECT, SELECT_SELECT, FIELD),
            asList(INSERT, INSERT_SELECT, SELECT, SELECT_SELECT, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_SELECT, SELECT, SELECT_FROM),
            asList(INSERT, INSERT_SELECT, SELECT, SELECT_FROM, TABLE),
            asList(INSERT, INSERT_SELECT, SELECT, SELECT_WHERE),
            asList(INSERT, INSERT_SELECT, SELECT, SELECT_START_WITH),
            asList(INSERT, INSERT_SELECT, SELECT, SELECT_CONNECT_BY),
            asList(INSERT, INSERT_SELECT, SELECT, SELECT_GROUP_BY),
            asList(INSERT, INSERT_SELECT, SELECT, SELECT_HAVING),
            asList(INSERT, INSERT_SELECT, SELECT, SELECT_ORDER_BY),
            asList(INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE),
            asList(INSERT, INSERT_RETURNING)
        ));
    }

    @Test
    public void test_UPDATE_SET_simple() {
        ctx.update(TABLE1)
           .set(FIELD_NAME1, "value")
           .getSQL();

        assertEvents(asList(
            asList(UPDATE),
            asList(UPDATE, UPDATE_UPDATE),
            asList(UPDATE, UPDATE_UPDATE, TABLE),
            asList(UPDATE, UPDATE_UPDATE, TABLE, TABLE_REFERENCE),
            asList(UPDATE, UPDATE_SET),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_REFERENCE),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_VALUE),
            asList(UPDATE, UPDATE_WHERE),
            asList(UPDATE, UPDATE_RETURNING)
        ));
    }

    @Test
    public void test_UPDATE_SET_twoValues() {
        ctx.update(TABLE1)
           .set(FIELD_NAME1, "value")
           .set(FIELD_DATE1, FIELD_DATE1)
           .getSQL();

        assertEvents(asList(
            asList(UPDATE),
            asList(UPDATE, UPDATE_UPDATE),
            asList(UPDATE, UPDATE_UPDATE, TABLE),
            asList(UPDATE, UPDATE_UPDATE, TABLE, TABLE_REFERENCE),
            asList(UPDATE, UPDATE_SET),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_REFERENCE),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_VALUE),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_REFERENCE),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_REFERENCE),
            asList(UPDATE, UPDATE_WHERE),
            asList(UPDATE, UPDATE_RETURNING)
        ));
    }

    @Test
    public void test_UPDATE_SET_rowValueExpressions() {

        // Postgres is known to support this syntax particularly well
        ctx.configuration().set(POSTGRES);

        ctx.update(TABLE1)
           .set(row(FIELD_NAME1,  FIELD_DATE1),
                row(val("value"), FIELD_DATE1))
           .getSQL();

        assertEvents(asList(
            asList(UPDATE),
            asList(UPDATE, UPDATE_UPDATE),
            asList(UPDATE, UPDATE_UPDATE, TABLE),
            asList(UPDATE, UPDATE_UPDATE, TABLE, TABLE_REFERENCE),
            asList(UPDATE, UPDATE_SET),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD_ROW),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD_ROW, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD_ROW, FIELD, FIELD_REFERENCE),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD_ROW, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD_ROW, FIELD, FIELD_REFERENCE),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD_ROW),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD_ROW, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD_ROW, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD_ROW, FIELD, FIELD_REFERENCE),
            asList(UPDATE, UPDATE_WHERE),
            asList(UPDATE, UPDATE_RETURNING)
        ));
    }

    @Test
    public void test_UPDATE_SET_WHERE() {
        ctx.update(TABLE1)
           .set(FIELD_NAME1, "value")
           .where(FIELD_ID1.eq(1))
           .getSQL();

        assertEvents(asList(
            asList(UPDATE),
            asList(UPDATE, UPDATE_UPDATE),
            asList(UPDATE, UPDATE_UPDATE, TABLE),
            asList(UPDATE, UPDATE_UPDATE, TABLE, TABLE_REFERENCE),
            asList(UPDATE, UPDATE_SET),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_REFERENCE),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_VALUE),
            asList(UPDATE, UPDATE_WHERE),
            asList(UPDATE, UPDATE_WHERE, CONDITION),
            asList(UPDATE, UPDATE_WHERE, CONDITION, CONDITION_COMPARISON),
            asList(UPDATE, UPDATE_WHERE, CONDITION, CONDITION_COMPARISON, FIELD),
            asList(UPDATE, UPDATE_WHERE, CONDITION, CONDITION_COMPARISON, FIELD, FIELD_REFERENCE),
            asList(UPDATE, UPDATE_WHERE, CONDITION, CONDITION_COMPARISON, FIELD),
            asList(UPDATE, UPDATE_WHERE, CONDITION, CONDITION_COMPARISON, FIELD, FIELD_VALUE),
            asList(UPDATE, UPDATE_RETURNING)
        ));
    }

    @Test
    public void test_CONDITION_simple() {
        ctx.render(FIELD_ID1.eq(1));

        assertEvents(asList(
            asList(CONDITION),
            asList(CONDITION, CONDITION_COMPARISON),
            asList(CONDITION, CONDITION_COMPARISON, FIELD),
            asList(CONDITION, CONDITION_COMPARISON, FIELD, FIELD_REFERENCE),
            asList(CONDITION, CONDITION_COMPARISON, FIELD),
            asList(CONDITION, CONDITION_COMPARISON, FIELD, FIELD_VALUE)
        ));
    }

    @Test
    public void test_CONDITION_NOT() {
        ctx.render(FIELD_ID1.eq(1).not());

        assertEvents(asList(
            asList(CONDITION),
            asList(CONDITION, CONDITION_NOT),
            asList(CONDITION, CONDITION_NOT, CONDITION),
            asList(CONDITION, CONDITION_NOT, CONDITION, CONDITION_COMPARISON),
            asList(CONDITION, CONDITION_NOT, CONDITION, CONDITION_COMPARISON, FIELD),
            asList(CONDITION, CONDITION_NOT, CONDITION, CONDITION_COMPARISON, FIELD, FIELD_REFERENCE),
            asList(CONDITION, CONDITION_NOT, CONDITION, CONDITION_COMPARISON, FIELD),
            asList(CONDITION, CONDITION_NOT, CONDITION, CONDITION_COMPARISON, FIELD, FIELD_VALUE)
        ));
    }

    @Test
    public void test_CONDITION_AND() {
        ctx.render(FIELD_ID1.eq(1).and(FIELD_NAME1.isNotNull()));

        assertEvents(asList(
            asList(CONDITION),
            asList(CONDITION, CONDITION_AND),
            asList(CONDITION, CONDITION_AND, CONDITION),
            asList(CONDITION, CONDITION_AND, CONDITION, CONDITION_COMPARISON),
            asList(CONDITION, CONDITION_AND, CONDITION, CONDITION_COMPARISON, FIELD),
            asList(CONDITION, CONDITION_AND, CONDITION, CONDITION_COMPARISON, FIELD, FIELD_REFERENCE),
            asList(CONDITION, CONDITION_AND, CONDITION, CONDITION_COMPARISON, FIELD),
            asList(CONDITION, CONDITION_AND, CONDITION, CONDITION_COMPARISON, FIELD, FIELD_VALUE),
            asList(CONDITION, CONDITION_AND, CONDITION),
            asList(CONDITION, CONDITION_AND, CONDITION, CONDITION_IS_NOT_NULL),
            asList(CONDITION, CONDITION_AND, CONDITION, CONDITION_IS_NOT_NULL, FIELD),
            asList(CONDITION, CONDITION_AND, CONDITION, CONDITION_IS_NOT_NULL, FIELD, FIELD_REFERENCE)
        ));
    }

    @Test
    public void test_CONDITION_OR() {
        ctx.render(FIELD_ID1.eq(1).or(FIELD_NAME1.isNull()));

        assertEvents(asList(
            asList(CONDITION),
            asList(CONDITION, CONDITION_OR),
            asList(CONDITION, CONDITION_OR, CONDITION),
            asList(CONDITION, CONDITION_OR, CONDITION, CONDITION_COMPARISON),
            asList(CONDITION, CONDITION_OR, CONDITION, CONDITION_COMPARISON, FIELD),
            asList(CONDITION, CONDITION_OR, CONDITION, CONDITION_COMPARISON, FIELD, FIELD_REFERENCE),
            asList(CONDITION, CONDITION_OR, CONDITION, CONDITION_COMPARISON, FIELD),
            asList(CONDITION, CONDITION_OR, CONDITION, CONDITION_COMPARISON, FIELD, FIELD_VALUE),
            asList(CONDITION, CONDITION_OR, CONDITION),
            asList(CONDITION, CONDITION_OR, CONDITION, CONDITION_IS_NULL),
            asList(CONDITION, CONDITION_OR, CONDITION, CONDITION_IS_NULL, FIELD),
            asList(CONDITION, CONDITION_OR, CONDITION, CONDITION_IS_NULL, FIELD, FIELD_REFERENCE)
        ));
    }

    @Test
    public void test_CONDITION_NULL() {
        ctx.render(FIELD_ID1.isNull());

        assertEvents(asList(
            asList(CONDITION),
            asList(CONDITION, CONDITION_IS_NULL),
            asList(CONDITION, CONDITION_IS_NULL, FIELD),
            asList(CONDITION, CONDITION_IS_NULL, FIELD, FIELD_REFERENCE)
        ));
    }

    @Test
    public void test_CONDITION_NOT_NULL() {
        ctx.render(FIELD_ID1.isNotNull());

        assertEvents(asList(
            asList(CONDITION),
            asList(CONDITION, CONDITION_IS_NOT_NULL),
            asList(CONDITION, CONDITION_IS_NOT_NULL, FIELD),
            asList(CONDITION, CONDITION_IS_NOT_NULL, FIELD, FIELD_REFERENCE)
        ));
    }

    @Test
    public void test_CONDITION_IN() {
        ctx.render(FIELD_ID1.in(1, 2));

        assertEvents(asList(
            asList(CONDITION),
            asList(CONDITION, CONDITION_IN),
            asList(CONDITION, CONDITION_IN, FIELD),
            asList(CONDITION, CONDITION_IN, FIELD, FIELD_REFERENCE),
            asList(CONDITION, CONDITION_IN, FIELD),
            asList(CONDITION, CONDITION_IN, FIELD, FIELD_VALUE),
            asList(CONDITION, CONDITION_IN, FIELD),
            asList(CONDITION, CONDITION_IN, FIELD, FIELD_VALUE)
        ));
    }

    @Test
    public void test_CONDITION_NOT_IN() {
        ctx.render(FIELD_ID1.notIn(1, 2));

        assertEvents(asList(
            asList(CONDITION),
            asList(CONDITION, CONDITION_NOT_IN),
            asList(CONDITION, CONDITION_NOT_IN, FIELD),
            asList(CONDITION, CONDITION_NOT_IN, FIELD, FIELD_REFERENCE),
            asList(CONDITION, CONDITION_NOT_IN, FIELD),
            asList(CONDITION, CONDITION_NOT_IN, FIELD, FIELD_VALUE),
            asList(CONDITION, CONDITION_NOT_IN, FIELD),
            asList(CONDITION, CONDITION_NOT_IN, FIELD, FIELD_VALUE)
        ));
    }

    @Test
    public void test_CONDITION_EXISTS() {

        // Omit "dual" with Postgres
        ctx.configuration().set(POSTGRES);
        ctx.render(exists(selectOne()));

        assertEvents(asList(
            asList(CONDITION),
            asList(CONDITION, CONDITION_EXISTS),
            asList(CONDITION, CONDITION_EXISTS, SELECT),
            asList(CONDITION, CONDITION_EXISTS, SELECT, SELECT_SELECT),
            asList(CONDITION, CONDITION_EXISTS, SELECT, SELECT_SELECT, FIELD),
            asList(CONDITION, CONDITION_EXISTS, SELECT, SELECT_SELECT, FIELD, FIELD_VALUE),
            asList(CONDITION, CONDITION_EXISTS, SELECT, SELECT_FROM),
            asList(CONDITION, CONDITION_EXISTS, SELECT, SELECT_WHERE),
            asList(CONDITION, CONDITION_EXISTS, SELECT, SELECT_START_WITH),
            asList(CONDITION, CONDITION_EXISTS, SELECT, SELECT_CONNECT_BY),
            asList(CONDITION, CONDITION_EXISTS, SELECT, SELECT_GROUP_BY),
            asList(CONDITION, CONDITION_EXISTS, SELECT, SELECT_HAVING),
            asList(CONDITION, CONDITION_EXISTS, SELECT, SELECT_ORDER_BY)
        ));
    }

    @Test
    public void test_CONDITION_NOT_EXISTS() {

        // Omit "dual" with Postgres
        ctx.configuration().set(POSTGRES);
        ctx.render(notExists(selectOne()));

        assertEvents(asList(
            asList(CONDITION),
            asList(CONDITION, CONDITION_NOT_EXISTS),
            asList(CONDITION, CONDITION_NOT_EXISTS, SELECT),
            asList(CONDITION, CONDITION_NOT_EXISTS, SELECT, SELECT_SELECT),
            asList(CONDITION, CONDITION_NOT_EXISTS, SELECT, SELECT_SELECT, FIELD),
            asList(CONDITION, CONDITION_NOT_EXISTS, SELECT, SELECT_SELECT, FIELD, FIELD_VALUE),
            asList(CONDITION, CONDITION_NOT_EXISTS, SELECT, SELECT_FROM),
            asList(CONDITION, CONDITION_NOT_EXISTS, SELECT, SELECT_WHERE),
            asList(CONDITION, CONDITION_NOT_EXISTS, SELECT, SELECT_START_WITH),
            asList(CONDITION, CONDITION_NOT_EXISTS, SELECT, SELECT_CONNECT_BY),
            asList(CONDITION, CONDITION_NOT_EXISTS, SELECT, SELECT_GROUP_BY),
            asList(CONDITION, CONDITION_NOT_EXISTS, SELECT, SELECT_HAVING),
            asList(CONDITION, CONDITION_NOT_EXISTS, SELECT, SELECT_ORDER_BY)
        ));
    }

    @Test
    public void test_CONDITION_BETWEEN() {
        ctx.render(FIELD_ID1.between(1).and(2));

        assertEvents(asList(
            asList(CONDITION),
            asList(CONDITION, CONDITION_BETWEEN),
            asList(CONDITION, CONDITION_BETWEEN, FIELD),
            asList(CONDITION, CONDITION_BETWEEN, FIELD, FIELD_REFERENCE),
            asList(CONDITION, CONDITION_BETWEEN, FIELD),
            asList(CONDITION, CONDITION_BETWEEN, FIELD, FIELD_VALUE),
            asList(CONDITION, CONDITION_BETWEEN, FIELD),
            asList(CONDITION, CONDITION_BETWEEN, FIELD, FIELD_VALUE)
        ));
    }

    @Test
    public void test_CONDITION_NOT_BETWEEN() {
        ctx.render(FIELD_ID1.notBetween(1, 2));

        assertEvents(asList(
            asList(CONDITION),
            asList(CONDITION, CONDITION_NOT_BETWEEN),
            asList(CONDITION, CONDITION_NOT_BETWEEN, FIELD),
            asList(CONDITION, CONDITION_NOT_BETWEEN, FIELD, FIELD_REFERENCE),
            asList(CONDITION, CONDITION_NOT_BETWEEN, FIELD),
            asList(CONDITION, CONDITION_NOT_BETWEEN, FIELD, FIELD_VALUE),
            asList(CONDITION, CONDITION_NOT_BETWEEN, FIELD),
            asList(CONDITION, CONDITION_NOT_BETWEEN, FIELD, FIELD_VALUE)
        ));
    }

    @Test
    public void test_CONDITION_BETWEEN_SYMMETRIC() {

        // Use Postgres for its native SYMMETRIC support
        ctx.configuration().set(POSTGRES);
        ctx.render(FIELD_ID1.betweenSymmetric(1).and(2));

        assertEvents(asList(
            asList(CONDITION),
            asList(CONDITION, CONDITION_BETWEEN_SYMMETRIC),
            asList(CONDITION, CONDITION_BETWEEN_SYMMETRIC, FIELD),
            asList(CONDITION, CONDITION_BETWEEN_SYMMETRIC, FIELD, FIELD_REFERENCE),
            asList(CONDITION, CONDITION_BETWEEN_SYMMETRIC, FIELD),
            asList(CONDITION, CONDITION_BETWEEN_SYMMETRIC, FIELD, FIELD_VALUE),
            asList(CONDITION, CONDITION_BETWEEN_SYMMETRIC, FIELD),
            asList(CONDITION, CONDITION_BETWEEN_SYMMETRIC, FIELD, FIELD_VALUE)
        ));
    }

    @Test
    public void test_CONDITION_NOT_BETWEEN_SYMMETRIC() {

        // Use Postgres for its native SYMMETRIC support
        ctx.configuration().set(POSTGRES);
        ctx.render(FIELD_ID1.notBetweenSymmetric(1, 2));

        assertEvents(asList(
            asList(CONDITION),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD, FIELD_REFERENCE),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD, FIELD_VALUE),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD, FIELD_VALUE)
        ));
    }

    @Test
    public void test_CONDITION_NOT_BETWEEN_SYMMETRIC_rowValueExpressions() {

        // Use Postgres for its native SYMMETRIC support
        ctx.configuration().set(POSTGRES);
        ctx.render(row(FIELD_ID1, FIELD_NAME1).notBetweenSymmetric(1, "a").and(2, "b"));

        assertEvents(asList(
            asList(CONDITION),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD_ROW),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD_ROW, FIELD),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD_ROW, FIELD, FIELD_REFERENCE),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD_ROW, FIELD),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD_ROW, FIELD, FIELD_REFERENCE),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD_ROW),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD_ROW, FIELD),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD_ROW, FIELD),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD_ROW),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD_ROW, FIELD),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD_ROW, FIELD),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD_ROW, FIELD, FIELD_VALUE)
        ));
    }

    private void assertEvents(List<List<Clause>> expected) {
        // This assertion is a bit more verbose to be able to detect errors more easily
        for (int i = 0; i < expected.size() && i < listener.clauses.size(); i++) {
            assertEquals("Mismatch at position " + i + ":", expected.get(i), listener.clauses.get(i));
        }

        if (expected.size() != listener.clauses.size()) {
            fail("Size mismatch:\n\tExpected: " + expected + "\n\tActual: " + listener.clauses);
        }
    }


    private static class SimpleListener implements VisitListener {

        List<List<Clause>> clauses = new ArrayList<List<Clause>>();

        @Override
        public void clauseStart(VisitContext context) {
            clauses.add(asList(context.clauses()));
        }

        @Override
        public void clauseEnd(VisitContext context) {}

        @Override
        public void visitStart(VisitContext context) {}

        @Override
        public void visitEnd(VisitContext context) {}

    }
}
