/*
 * Copyright (c) 2008, SQL Power Group Inc.
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in
 *       the documentation and/or other materials provided with the
 *       distribution.
 *     * Neither the name of SQL Power Group Inc. nor the names of its
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ca.sqlpower.architect.swingui.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import ca.sqlpower.architect.swingui.ArchitectFrame;
import ca.sqlpower.architect.swingui.ArchitectSwingSession;
import ca.sqlpower.architect.swingui.ContainerPane;
import ca.sqlpower.architect.swingui.TablePane;

public class AlignTableAction extends AbstractArchitectAction{
   
    private static final Logger logger = Logger.getLogger(AlignTableAction.class);

    private boolean isHorizontal;

    public AlignTableAction(ArchitectSwingSession session, String actionName, String actionDescription,
            boolean isHorizontal) {
        super(session, actionName, actionDescription);
        this.isHorizontal = isHorizontal;
    }
    
    public AlignTableAction(ArchitectFrame frame, String actionName, String actionDescription,
            boolean isHorizontal) {
        super(frame, actionName, actionDescription);
        this.isHorizontal = isHorizontal;
    }

    /**
     * The action for aligning tables either horizontally or vertically.
     */
    public void actionPerformed(ActionEvent e) {
        List<TablePane> selection = new ArrayList<TablePane>();
        for (ContainerPane<?, ?> cp : getSession().getPlayPen().getSelectedContainers()) {
            if (cp instanceof TablePane) {
                selection.add((TablePane) cp);
            }
        }
        selection = Collections.unmodifiableList(selection);
        if (selection.size() < 2) {
            JOptionPane.showMessageDialog(getPlaypen(), Messages.getString("AlignTableAction.selectAtLeastTwoTables")); //$NON-NLS-1$
        } else if (selection.size() >= 2) {
            int min = Integer.MAX_VALUE;
            getPlaypen().startCompoundEdit("Aligning tables"); //$NON-NLS-1$
            logger.debug("Starting to align " + selection.size() + "tables"); //$NON-NLS-1$ //$NON-NLS-2$
            if (!isHorizontal) {
                for (int i = 0; i < selection.size(); i++) {
                    if (selection.get(i).getX() < min) {
                        min = (selection.get(i)).getX();
                    }
                }
                for (int i = 0; i < selection.size(); i++) {
                    selection.get(i).setLocation(min, selection.get(i).getY());
                }
            } else {
                for (int i = 0; i < selection.size(); i++) {
                    if (selection.get(i).getY() < min) {
                        min = (selection.get(i)).getY();
                    }
                }
                for (int i = 0; i < selection.size(); i++) {
                    selection.get(i).setLocation(selection.get(i).getX(), min);
                }
            }
            getPlaypen().endCompoundEdit("Ending the alignment of tables"); //$NON-NLS-1$
        } else {
            JOptionPane.showMessageDialog(getPlaypen(), Messages.getString("AlignTableAction.selectAtLeastTwoTables")); //$NON-NLS-1$
        }
    }
}
