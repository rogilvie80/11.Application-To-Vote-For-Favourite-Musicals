/*
 * MessageSubscriber.java
 *
 * Created on 08 February 2010, 00:53
 *
 */

package client;

import java.awt.EventQueue;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 *
 * @author M362
 *
 * Based on the MessageSubscriber of Activity 9.6
 */
public class MessageSubscriber implements MessageListener
{
   private VotingClient pollClient;
   
   
   /**
    * MessageSubscriber constructor
    * 
    * 
    * @param pollClient  The VotingClient argument
    *                    which will use this
    *                    MessageSubscriber
    */
   public MessageSubscriber(VotingClient pollClient)
   {
      
      this.pollClient = pollClient;
      try
      {
         // Create initial lookup context
         context = new InitialContext();
         
         // Look up topic connection factory and topic
         lookupFactories();
      }
      
      catch (NamingException e)
      {
         System.out.println("Unable to do lookup: " + e);
         System.exit(1);
      }
      
      try
      {
         // Create a connection
         topicConnection =
          topicConnectionFactory.createTopicConnection();
         
         // Then create a session for the connection
         topicSession =
          topicConnection.createTopicSession(false,
          Session.AUTO_ACKNOWLEDGE);
         
         // Next create the topic subscriber
         topicSubscriber =
          topicSession.createSubscriber(topic);
         
         // Create a listener - an instance of the
         // MessageSubscriber class itself
         topicListener = this;
         
         // Register the listener with the topic subscriber
         topicSubscriber.setMessageListener(topicListener);
         
         topicConnection.start();
         
         // Report if connection successful
         System.out.println("Successfully subscribed to topic");
      }
      catch (Exception e)
      {
         e.printStackTrace();
         cleanUp();
      }
   }
   
   // This method is invoked automatically when a JMS message is received
   public void onMessage(Message message)
   {
      TextMessage textMessage = null;
      
      try
      {
         if ((message != null) && (message instanceof TextMessage))
         {
            textMessage = (TextMessage) message;
            final String newVote = textMessage.getText();
            System.out.println("DEBUG received message " + newVote);
            
            
             /* IMPORTANT:
              * to guard against GUI threading problems,
              * any code affecting the VotingClient display
              * should be placed within the run method below.
              */
            EventQueue.invokeLater(new Runnable()
            {
               public void run()
               {                
                  // TODO for part (b)(ii)
                  // insert code here to update the VotingClient
                  // with the new vote
                   pollClient.updateVoting(newVote);
               }
            });
            
         }
      }
      
      catch (Exception e)
      {
         e.printStackTrace();
         cleanUp();
      }
   }
   
   // Helper methods for MessageSubscriber class
   
   private void lookupFactories() throws NamingException
   {
      // Hard-wired names for this simple demonstration
      // Note if these "administered objects" have not yet been
      // created on the server a NamingException will occur
      String factoryName = "VotingConnectionFactory";
      String topicName = "VotingTopic";
      
      // This is the FACTORY lookup
      topicConnectionFactory = (TopicConnectionFactory)context.lookup(factoryName);
      
      // This is the DESTINATION lookup
      topic = (Topic) context.lookup(topicName);
   }
   
   private void cleanUp()
   {
      if (topicConnection != null)
      {
         try
         {
            topicConnection.close();
         }
         catch (JMSException e)
         {
            e.printStackTrace();
         }
      }
   }
   
   // Declaration block for MessageSubscriber
   private Context context = null;
   private TopicConnectionFactory topicConnectionFactory = null;
   private TopicConnection topicConnection = null;
   private TopicSession topicSession = null;
   private Topic topic = null;
   private TopicSubscriber topicSubscriber = null;
   private MessageSubscriber topicListener = null;
   private TextMessage message = null;
}
// --- End of class MessageSubscriber
