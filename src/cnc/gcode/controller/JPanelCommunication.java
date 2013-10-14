/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cnc.gcode.controller;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author patrick
 */
public class JPanelCommunication extends javax.swing.JPanel implements IGUIEvent{

    private IEvent GUIEvent=null;
    private static volatile boolean setview=false;

    /**
     * Creates new form JPanelCommunication
     */
    public JPanelCommunication() {
        initComponents();
        
        Communication.getInstance().addResiveEvent(new Communication.IResivedLines() {
            @Override
            public void resived(String[] lines) {
                for(String line: lines)
                {
                    ((DefaultComboBoxModel<SendListElement>)jLCInOut.getModel()).addElement(new SendListElement(line, SendListElement.EType.IN));
                    if(((DefaultComboBoxModel<SendListElement>)jLCInOut.getModel()).getSize()>100)
                        ((DefaultComboBoxModel<SendListElement>)jLCInOut.getModel()).removeElementAt(0);
                    if(!setview)
                    {
                        setview=true;
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                setview=false;
                                jLCInOut.ensureIndexIsVisible(jLCInOut.getModel().getSize()-1);
                            }
                        });
                    }
                }
            }
        });
        
        Communication.getInstance().addSendEvent(new Communication.ISend() {
            @Override
            public void send(String cmd) {
                //Add to list 
                ((DefaultComboBoxModel<SendListElement>) jLCInOut.getModel()).addElement(new SendListElement(cmd, SendListElement.EType.OUT));
                if(((DefaultComboBoxModel<SendListElement>)jLCInOut.getModel()).getSize()>100)
                    ((DefaultComboBoxModel<SendListElement>)jLCInOut.getModel()).removeElementAt(0);
                if(!setview)
                {
                    setview=true;
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            setview=false;
                            jLCInOut.ensureIndexIsVisible(jLCInOut.getModel().getSize() - 1);
                        }
                    });
                }
            }
        });
    }

    
    @Override
    public void setGUIEvent(IEvent event)
    {
        GUIEvent=event;
    }

    @Override
    public void updateGUI(boolean serial, boolean isworking)
    {
        jBSend.setEnabled(!isworking && serial);
    }
    
    private void fireupdateGUI()
    {
        if(GUIEvent==null)
            throw new RuntimeException("GUI EVENT NOT USED!");
        GUIEvent.fired();
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        jLCInOut = new javax.swing.JList();
        jTFSend = new javax.swing.JTextField();
        jBSend = new javax.swing.JButton();

        jLCInOut.setModel(new javax.swing.DefaultComboBoxModel(new SendListElement[0]));
        jLCInOut.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jLCInOut.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jLCInOutValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(jLCInOut);

        jTFSend.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTFSendKeyReleased(evt);
            }
        });

        jBSend.setText("Send");
        jBSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBSendActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 761, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTFSend)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBSend))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFSend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBSend)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jLCInOutValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jLCInOutValueChanged
        if(jLCInOut.getSelectedIndex()!=-1)
            jTFSend.setText(((SendListElement)jLCInOut.getModel().getElementAt(jLCInOut.getSelectedIndex())).getText());
    }//GEN-LAST:event_jLCInOutValueChanged

    private void jTFSendKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFSendKeyReleased
        if(evt.getKeyCode()== KeyEvent.VK_ENTER)
            jBSendActionPerformed(new ActionEvent(evt.getSource(),evt.getID(),evt.toString()));
    }//GEN-LAST:event_jTFSendKeyReleased

    private void jBSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBSendActionPerformed
        if(!jTFSend.getText().equals("") && Communication.getInstance().isConnect())
        {
            if(Communication.getInstance().isbussy())
            {
                JOptionPane.showMessageDialog(this, "An other command is in Progress!");
                return;
            }
            Communication.getInstance().send(jTFSend.getText());
        }
    }//GEN-LAST:event_jBSendActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBSend;
    private javax.swing.JList jLCInOut;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTFSend;
    // End of variables declaration//GEN-END:variables
}
