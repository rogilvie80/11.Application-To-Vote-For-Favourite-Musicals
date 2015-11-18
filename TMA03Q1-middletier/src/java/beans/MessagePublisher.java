/*
 * MessagePublisher.java
 *
 * Created on 19 March 2008, 14:25
 *
 */

package beans;

import javax.jms.*;
import javax.naming.*;

public class MessagePublisher
{
   // Do JNDI lookup for CONNECTION FACTORY and TOPIC
   public MessagePublisher(){
     try
     {
         // Create initial JNDI lookup context
         context = new InitialContext();
         
         // Look up topic connection factory and topic
         lookupFactories();
      }
      catch (NamingException e)
      {
         e.printStackTrace();
         System.exit(1);
      }   
   }
           
   // Publish a text message to JMS 
   // For simplicity, opens and closes JMS connection for each message 
   public void publish(String message)
   {    
      TopicSession topicSession;
      TopicPublisher topicPublisher;
      TextMessage textMessage;
      
      try
      {
         // Create a connection
         topicConnection = topicConnectionFactory.createTopicConnection();
         
         // Then create a session for the connection
         topicSession =
               topicConnection.createTopicSession(false,
               Session.AUTO_ACKNOWLEDGE);
         
         // Next create the topic publisher
         topicPublisher = topicSession.createPublisher(topic);
         
         // Create a textMessage object for the session
         textMessage = topicSession.createTextMessage();
              
         textMessage.setText(message);
         topicPublisher.publish(textMessage);
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      finally
      {
         cleanUp();
      }
      
   }
   
   // Helper methods   
   private void lookupFactories() throws NamingException
   {
      // Hard-wired names for this simple demonstration
      // Note if these administered objects have not yet been
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
   
   
   // Declaration block
   private Context context = null;
   private TopicConnectionFactory topicConnectionFactory = null;
   private Topic topic = null;
   private TopicConnection topicConnection = null;

}
