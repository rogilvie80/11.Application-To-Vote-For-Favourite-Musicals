/*
 * VotingClient.java
 *
 * 2010, 2013
 */

package client;

import beans.VotingRemote;
import java.awt.Color;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import javax.naming.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import server.Voting;

/**
 * @author M362
 *
 * Libraries needed 
 *
 * gf-client.jar
 * javax.ejb.jar
 * javax.jms.jar
 *
 * normally found in subfolder
 *
 * glassfish/modules
 *
 * within the folder where your Glassfish server is installed
 *
 * Version 2.0
 */

public class VotingClient extends JFrame
{
   private static Color houseBackground = new Color(0xEE, 0xEE, 0xD0);
   
   Voting poll;
   Map<String, Integer> votes; //same structure as on Server side
   
   // TODO add code here for part (a) and part (b)(ii)
   Context context = null;
   VotingRemote remoteBean = null;
   MessageSubscriber ms = null;

   public VotingClient()
   {
      initComponents();
      this.getContentPane().setBackground(houseBackground);
      setIconImage((new ImageIcon(getClass().getResource("/images/musicon.jpg")).getImage()));
      setExtendedState(JFrame.NORMAL);
      this.setAlwaysOnTop(true);
      
      // TODO add code here for part (a) and part (b)(ii)
      // For JNDI lookup use the bean's portable name
      // "java:global/TMA03Q1-middletier/VotingBean"
      try
      {
          context = new InitialContext();
          remoteBean = (VotingRemote) context.lookup("java:global/TMA03Q1-middletier/VotingBean");
      }
      catch (NamingException ex)
      {
          System.out.println("Lookup failed: " + ex);
      }
      ms = new MessageSubscriber(this);
      
      // Set up RMI with server
      try
      {
         // Get a reference to the object bound by the remote server
         Registry registry = LocateRegistry.getRegistry();
         poll = (Voting) registry.lookup("poll");
         // Generate a random client id
         setTitle("Visitor ID: " + (new Random()).nextInt(100000000));
         System.out.println("Made contact with server...");
         
         votes = null;
         try
         {
            votes = poll.getVotes();
         }
         catch (RemoteException e)
         {
            System.out.println("Remote exception");
            e.printStackTrace();
         }
      }
      catch (Exception e)
      {
         System.out.println("Could not locate remote object. Client will exit.");
         System.exit(1);
      }
      
      try
      {
         showPoll();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      
   }
   
   private void showPoll()
   {
      try
      {
         votes = poll.getVotes();
      }
      catch (Exception e)
      {
         System.out.println("Remote exception");
         e.printStackTrace();
      }
      
      // Vector is a legacy class but is the type JList
      // objects expect as the argument to setListData().
      // Please ignore any warnings from NetBeans!
      Vector<Object> candidates = new Vector<Object>();
      for (String each : votes.keySet())
      {
         candidates.add(each + ": " + votes.get(each));
      }
      candidateList.setListData(candidates);
      
      textArea.setText("Current votes at " + new Date() + " are listed above.\n");
   }
   
   public void castVote()
   {
      String vote = (String) candidateList.getSelectedValue();
      if (vote != null)
      {
         vote = vote.split(":")[0]; // Extract name...
         System.out.println("Casting vote for " + vote);
         try
         {
            // TODO change the following statement for part (a)
            //poll.recordVote(vote); // ...and vote!
            remoteBean.castVote(vote); 
         }
         catch (Exception e)
         {
            e.printStackTrace();
         }
      }
      else
      {
         textArea.append("Please choose a musical.\n");
      }
   }
   
   /**
    * When a new vote is received reports this in the text area
    * and then shows the new polling figures. 
    */
   public void updateVoting(String newVote)
   {
      textArea.append(newVote + "\n");   // report message
      showPoll();
   }
   
   /**
    * This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
   // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
   private void initComponents() {
      jScrollPane1 = new javax.swing.JScrollPane();
      candidateList = new javax.swing.JList();
      jLabel1 = new javax.swing.JLabel();
      showVotingButton = new javax.swing.JButton();
      castVoteButton = new javax.swing.JButton();
      jLabel2 = new javax.swing.JLabel();
      jLabel3 = new javax.swing.JLabel();
      jScrollPane2 = new javax.swing.JScrollPane();
      textArea = new javax.swing.JTextArea();
      jLabel4 = new javax.swing.JLabel();
      jLabel5 = new javax.swing.JLabel();

      getContentPane().setLayout(null);

      setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
      setTitle("Poll Client");
      jScrollPane1.setViewportView(candidateList);

      getContentPane().add(jScrollPane1);
      jScrollPane1.setBounds(10, 130, 140, 90);

      jLabel1.setText("Cast your vote now!");
      getContentPane().add(jLabel1);
      jLabel1.setBounds(170, 130, 125, 20);

      showVotingButton.setText("Show Voting");
      showVotingButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            showVotingButtonActionPerformed(evt);
         }
      });

      getContentPane().add(showVotingButton);
      showVotingButton.setBounds(170, 190, 165, 23);

      castVoteButton.setText("Cast Vote");
      castVoteButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            castVoteButtonActionPerformed(evt);
         }
      });

      getContentPane().add(castVoteButton);
      castVoteButton.setBounds(170, 160, 165, 23);

      jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/guitar.jpg")));
      getContentPane().add(jLabel2);
      jLabel2.setBounds(10, 10, 180, 55);

      jLabel3.setFont(new java.awt.Font("Tahoma", 2, 14));
      jLabel3.setText("Vote for the Greatest Musical of all Time");
      getContentPane().add(jLabel3);
      jLabel3.setBounds(10, 80, 250, 40);

      textArea.setEditable(false);
      jScrollPane2.setViewportView(textArea);

      getContentPane().add(jScrollPane2);
      jScrollPane2.setBounds(10, 260, 460, 50);

      jLabel4.setFont(new java.awt.Font("Tahoma", 2, 14));
      jLabel4.setText("The Music Store Hall of Fame");
      getContentPane().add(jLabel4);
      jLabel4.setBounds(200, 10, 260, 50);

      jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/hall-fame.jpg")));
      getContentPane().add(jLabel5);
      jLabel5.setBounds(380, 10, 70, 50);

      java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
      setBounds((screenSize.width-493)/2, (screenSize.height-354)/2, 493, 354);
   }// </editor-fold>//GEN-END:initComponents
   
    private void castVoteButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_castVoteButtonActionPerformed
    {//GEN-HEADEREND:event_castVoteButtonActionPerformed
       castVote();
    }//GEN-LAST:event_castVoteButtonActionPerformed
    
    private void showVotingButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_showVotingButtonActionPerformed
    {//GEN-HEADEREND:event_showVotingButtonActionPerformed
       showPoll();
    }//GEN-LAST:event_showVotingButtonActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
       java.awt.EventQueue.invokeLater(new Runnable()
       {
          public void run()
          {
             new VotingClient().setVisible(true);
          }
       });
    }
    
   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JList candidateList;
   private javax.swing.JButton castVoteButton;
   private javax.swing.JLabel jLabel1;
   private javax.swing.JLabel jLabel2;
   private javax.swing.JLabel jLabel3;
   private javax.swing.JLabel jLabel4;
   private javax.swing.JLabel jLabel5;
   private javax.swing.JScrollPane jScrollPane1;
   private javax.swing.JScrollPane jScrollPane2;
   private javax.swing.JButton showVotingButton;
   private javax.swing.JTextArea textArea;
   // End of variables declaration//GEN-END:variables
   
}
