/*
 * Copyright (C) 2017 Leonardo Gonzalez Caracosa
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package escaner.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import escaner.services.DeviceStatus;

public class FormatoTabla extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	private TablePanel tablePanel;
    
	public FormatoTabla(TablePanel tablePanel) {
		this.tablePanel = tablePanel;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
        setBackground(new Color(20,20,20));
        table.setForeground(Color.black);
        setToolTipText(null);
        switch(Integer.parseInt(table.getValueAt(row,4).toString())) {
            case DeviceStatus.EXIST: setForeground(new Color(229,226,221)); break;
            case DeviceStatus.OFFLINE: setForeground(new Color(102,100,96)); break;
            case DeviceStatus.CHANGED:
            	setForeground(new Color(59, 131, 189));
                if(column == 2) {
                	String tooltip = tablePanel.searchPrevious(value.toString());
                	if(tooltip == null) {
                		break;
                	}
                    setToolTipText("IP Anterior: " + tooltip);
                }
                break;
            case DeviceStatus.NEW: setForeground(new Color(203, 50, 52)); break;
            case DeviceStatus.UNKNOWN: setForeground(new Color(229,190,1)); break;
        }

        super.getTableCellRendererComponent(table, value, selected, focused, row, column);
        return this;
    }
}