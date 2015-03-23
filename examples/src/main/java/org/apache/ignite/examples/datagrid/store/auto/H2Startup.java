/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ignite.examples.datagrid.store.auto;

import org.apache.ignite.*;
import org.h2.jdbcx.*;
import org.h2.tools.*;

import java.io.*;
import java.sql.*;

/**
 * Start H2 database TCP server in order to access sample in-memory database from other processes.
 */
public class H2Startup {
    /** */
    private static final String DB_SCRIPT =
        "create table PERSON(id bigint not null, first_name varchar(50), last_name varchar(50), PRIMARY KEY(id));";

    /**
     * Start H2 database TCP server.
     *
     * @param args Command line arguments, none required.
     * @throws IgniteException If start H2 database TCP server failed.
     */
    public static void main(String[] args) throws IgniteException {
        try {
            // Start H2 database TCP server in order to access sample in-memory database from other processes.
            Server.createTcpServer("-tcpDaemon").start();

            // Try to connect to database TCP server.
            JdbcConnectionPool dataSrc = JdbcConnectionPool.create("jdbc:h2:tcp://localhost/mem:ExampleDb", "sa", "");

            // Load sample data into database.
            RunScript.execute(dataSrc.getConnection(), new StringReader(DB_SCRIPT));
        }
        catch (SQLException e) {
            throw new IgniteException("Failed to start database TCP server", e);
        }

        try {
            do {
                System.out.println("Type 'q' and press 'Enter' to stop H2 TCP server...");
            }
            while ('q' != System.in.read());
        }
        catch (IOException ignored) {
            // No-op.
        }
    }
}
