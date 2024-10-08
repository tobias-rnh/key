/* This file is part of KeY - https://key-project.org
 * KeY is licensed under the GNU General Public License Version 2
 * SPDX-License-Identifier: GPL-2.0-only */
package de.uka.ilkd.key.gui.notification;

/**
 * Notifies the user when a proof task is abandoned.
 *
 * @author bubel
 */
public class AbandonNotification extends NotificationTask {

    /*
     * (non-Javadoc)
     *
     * @see de.uka.ilkd.key.gui.notification.NotificationTask#getEventID()
     */
    @Override
    public NotificationEventID getEventID() {
        return NotificationEventID.TASK_ABANDONED;
    }

}
