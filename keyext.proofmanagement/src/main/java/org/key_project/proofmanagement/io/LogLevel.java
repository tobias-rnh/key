/* This file is part of KeY - https://key-project.org
 * KeY is licensed under the GNU General Public License Version 2
 * SPDX-License-Identifier: GPL-2.0-only */
package org.key_project.proofmanagement.io;

/**
 * Denotes the severity level of a message (usually generated by a checker)
 * to print by a {@link Logger}.
 *
 * @author Wolfram Pfeifer
 */
public enum LogLevel {
    // be careful: we rely on the order here to compare by severity
    /** Log level of debug messages. */
    DEBUG("[    Debug    ] "),

    /** Default log level for messages without prefix. */
    DEFAULT(""),

    /** Log level for information messages with low severity. */
    INFO("[ Information ] "),

    /** Log level for warnings. */
    WARNING("[   Warning   ] "),

    /**
     * Log level for critical errors where further checks have to be aborted.
     * This should always be printed by the logger.
     */
    ERROR("[    Error    ] ");

    /** prefix of the log level */
    private final String prefix;

    /**
     * Creates a new LogLevel with the given prefix
     *
     * @param prefix print prefix of the log level
     */
    LogLevel(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String toString() {
        return prefix;
    }
}
