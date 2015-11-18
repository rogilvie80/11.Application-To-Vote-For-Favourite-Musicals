/*
 * VotingBean.java
 *
 * 2010, 2013
 */

package beans;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.ejb.Stateless;
import server.Voting;

/**
 *
 * @author M362
 */
@Stateless(mappedName="java:global/TMA03Q1-middletier/VotingBean")
public class VotingBean implements VotingRemote
{
   
   Voting poll;
   
   // TODO add code here for part (b)(i)
   MessagePublisher mp = null;
   
   /** Creates a new instance of VotingBean */
   public VotingBean()
   {
      // TODO add code here for part (b)(i)
      mp = new MessagePublisher();
      
      try
      {
         // Get a reference to the object bound by the remote server
         Registry registry = LocateRegistry.getRegistry();
         poll = (Voting) registry.lookup("poll");
      }
      catch (RemoteException ex)
      {
         ex.printStackTrace();
      }
      catch (NotBoundException ex)
      {
         ex.printStackTrace();
      }
   }
   
   public void castVote(String candidate)
   {
      System.out.println("Method castVote() in VotingBean invoked");
      try
      {
         poll.recordVote(candidate);
      }
      catch (RemoteException ex)
      {
         ex.printStackTrace();
      }
      catch (InterruptedException ex)
      {
         ex.printStackTrace();
      }
      
      // TODO add code here for part (b)(i)
      mp.publish("a vote was cast for " + candidate);
   }
}
