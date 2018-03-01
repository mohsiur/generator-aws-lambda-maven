package <%=packageName%>.utils.AmazonWebServices;

/**
 * AWS SQS Library
 * @author Kartik Reddy - https://github.com/
 * @contributors Mohsiur Rahman - https://github.com/mohsiur
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.ChangeMessageVisibilityRequest;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.DeleteQueueResult;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.PurgeQueueRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class SQS 
{
	private AmazonSQS sqs;
	private String url;
	private boolean queueExists;
	private boolean queueEmpty;
	
	//	Can retrieve a max of 10 messages from SQS.
	static final int maximumRetrievableMessages = 10;
	
	
	//	Default Constructor
	public SQS()
	{
		sqs = new AmazonSQSClient();
		queueExists = false;
		queueEmpty = false;
	}
	
	//	Parameterized constructor with URL
	public SQS(String url)
	{
		sqs = new AmazonSQSClient();
		this.url = url;
	}
	
	//	Parameterized constructor with access keys
	public SQS(String swfAccessId, String swfSecretKey)
	{
		sqs = new AmazonSQSClient(new BasicAWSCredentials(swfAccessId, swfSecretKey));
	}
	
	//	Parameterized constructor with URLs
	public SQS(String swfAccessId, String swfSecretKey, String url)
	{
		sqs = new AmazonSQSClient(new BasicAWSCredentials(swfAccessId, swfSecretKey));
		this.url = url;
	}
	
	
	/************************************************
	 * 	Getting A new Amazon SQS client.			*
	 ************************************************/
	
	/*	Get a new SQS Clients
	 */
	public AmazonSQSClient getSQSClient()
	{
		return new AmazonSQSClient();
	}
	
	/*	Get a new SQS Clients
	 */
	public AmazonSQSClient getSQSClient(String swfAccessId, String swfSecretKey)
	{
		return  new AmazonSQSClient(new BasicAWSCredentials(swfAccessId, swfSecretKey));
	}
	
	/*	Get the queue URL
	 * 
	 */
	public String getQueueUrl()
	{
		return this.url;
	}
	
	
	/*	Returns true if queue is empty.
	 * 
	 */
	public boolean isQueueEmpty()
	{
		return queueEmpty;
	}
	
	/*	Returns true if queue does exist.
	 * 
	 */
	public boolean doesQueueExist()
	{
		return queueExists;
	}
	
	
	/************************************************
	 * 	Setting A new Amazon SQS client.			*
	 ************************************************/
	/*	Set a new SQS Clients
	 */
	public void setSQSClient()
	{
		sqs = new AmazonSQSClient();
	}
	
	/*	Set a new SQS Clients
	 */
	public void setSQSClient(String swfAccessId, String swfSecretKey)
	{
		sqs = new AmazonSQSClient(new BasicAWSCredentials(swfAccessId, swfSecretKey));
	}
		
	/*	Set the queue URL
	 * 
	 */
	public void setQueueUrl(String url)
	{
		this.url = url;
	}
	
	/************************************************
	 * 	Purge Queue									*
	 ************************************************/
	/*	Purge the default queue
	 * 
	 */
	public void purgeQueue()
	{
		PurgeQueueRequest request = new PurgeQueueRequest();
		request.setQueueUrl(this.url);		
		sqs.purgeQueue(request);
	}
	
	/*	Purge the default queue
	 * 
	 */
	public void purgeQueue(String url)
	{
		PurgeQueueRequest request = new PurgeQueueRequest();
		request.setQueueUrl(url);		
		sqs.purgeQueue(request);
	}
	
	/************************************************
	 * 	Send Message functions						*
	 ************************************************/
	/*	Send message to the default URL
	 * 
	 */
	public void sendMessage(String message)
	{
		SendMessageRequest request = new SendMessageRequest();
		request.setQueueUrl(this.url);
        request.setMessageBody(message);
        sqs.sendMessage(request);
	}
	
	/*	Send message to the default URL n times
	 * @return number of message been send
	 */
	public void sendMessage(String message, int n)
	{
		SendMessageRequest request = new SendMessageRequest();
		request.setQueueUrl(this.url);
        request.setMessageBody(message);
        while(n-->0)	sqs.sendMessage(request);
	}
	
	/*	Send message to a customised URL
	 * 
	 */
	public void sendMessage(String message, String url)
	{
		SendMessageRequest request = new SendMessageRequest();
		request.setQueueUrl(url);
        request.setMessageBody(message);
        sqs.sendMessage(request);
	}
	
	/*	Send message to a customised URL
	 * 
	 * @return number of message been send
	 */
	public void sendMessage(String message, String url, int n)
	{
		SendMessageRequest request = new SendMessageRequest();
		request.setQueueUrl(url);
        request.setMessageBody(message);
        while(n-->0)	sqs.sendMessage(request);
	}
	
	
	/*	Send message to the default URL
	 * 
	 */
	public void sendMessageWithMessageId(String message, String messageGroupId)
	{
		SendMessageRequest request = new SendMessageRequest(this.url, message);
		request.setMessageGroupId(messageGroupId);
		sqs.sendMessage(request);
	}
	/************************************************
	 * 	Get Message functions						*
	 ************************************************/
	
	/*	Get unique messages from the queue
	 * 
	 */
	public String getMessage()
	{
		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(url);
				
		receiveMessageRequest.setMaxNumberOfMessages(1);
	        
		receiveMessageRequest.setMessageAttributeNames(new ArrayList<String>(Arrays.asList("All")));
		  
		ReceiveMessageResult receiveMessageResult = sqs.receiveMessage(receiveMessageRequest);

	    /**
	     * Receives messages from the queue as list
	     *
	     * **/
	    List<Message> messages = receiveMessageResult.getMessages();
	   
	    if(messages.size() == 0)
	    	return "QueueEmpty";
	    
	    for(Message message: messages)
	    {
	    	return message.getBody();
	    }
	    
		return "QueueEmpty";
	}
	
	/*	Get n messages from the queue
	 * 
	 */
	public List<String> getMessages(int n)
	{
		List<String> sessionIds = new ArrayList<String>();
		List<String> queueMessage = new ArrayList<String>();
		int count = 0;
		boolean nFlag = false;
		
		//	Get all the items from the queue.
		while(true)
		{
				
			ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(this.url);
				
			receiveMessageRequest.setMaxNumberOfMessages((SQS.maximumRetrievableMessages > n) ? n:SQS.maximumRetrievableMessages);
			receiveMessageRequest.setVisibilityTimeout(3600);
		        
			receiveMessageRequest.setMessageAttributeNames(new ArrayList<String>(Arrays.asList("All")));
		  
		    ReceiveMessageResult receiveMessageResult = sqs.receiveMessage(receiveMessageRequest);

		    /**
		     * Receives messages from the queue as list
		     *
		     * **/
		    List<Message> messages = receiveMessageResult.getMessages();
		    
		    if(messages.size() == 0)
		    	break;
		        
		    for(Message message: messages)
		    {	
		    	String sessionId = message.getReceiptHandle();
		        sessionIds.add(sessionId);
		        
		        if(count++ >= n) nFlag  = !nFlag;
		        else queueMessage.add(message.getBody());
		    }
		    
		    if(nFlag)	break;
		}
		
		//	Change the visibility to unlocked
		unlockMessages(sessionIds);
		
		return queueMessage;
	}
	
	/*	Get unique messages from the queue
	 * 
	 */
	public Set<String> getAllUniqueMessages()
	{
		List<String> sessionIds = new ArrayList<String>();
		Set<String> queueMessage = new HashSet<String>();
		
		//	Get all the items from the queue.
		while(true)
		{
				
			ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(url);
				
			receiveMessageRequest.setMaxNumberOfMessages(SQS.maximumRetrievableMessages);
			receiveMessageRequest.setVisibilityTimeout(3600);
		        
			receiveMessageRequest.setMessageAttributeNames(new ArrayList<String>(Arrays.asList("All")));
		  
		    ReceiveMessageResult receiveMessageResult = sqs.receiveMessage(receiveMessageRequest);

		    /**
		     * Receives messages from the queue as list
		     *
		     * **/
		    List<Message> messages = receiveMessageResult.getMessages();
		   
		    if(messages.size() == 0)
		    	break;
		        
		    for(Message message: messages)
		    {
		        	
		    	queueMessage.add(message.getBody());
		    	String sessionId = message.getReceiptHandle();
		        	
		        sessionIds.add(sessionId);
		    }
		}
		
		//	Change the visibility to unlocked
		unlockMessages(sessionIds);
			
		return queueMessage;
	}
	
	/*	Get all messages from the queue
	 * 
	 */
	public List<String> getAllMessages()
	{
		List<String> sessionIds = new ArrayList<String>();
		List<String> queueMessage = new ArrayList<String>();
		
		//	Get all the items from the queue.
		while(true)
		{
				
			ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(this.url);
				
			receiveMessageRequest.setMaxNumberOfMessages(SQS.maximumRetrievableMessages);
			receiveMessageRequest.setVisibilityTimeout(3600);
		        
			receiveMessageRequest.setMessageAttributeNames(new ArrayList<String>(Arrays.asList("All")));
		  
		    ReceiveMessageResult receiveMessageResult = sqs.receiveMessage(receiveMessageRequest);

		    /**
		     * Receives messages from the queue as list
		     *
		     * **/
		    List<Message> messages = receiveMessageResult.getMessages();
		    if(messages.size() == 0)
		    	break;
		        
		    for(Message message: messages)
		    {
		        	
		    	queueMessage.add(message.getBody());
		    	String sessionId = message.getReceiptHandle();
		        	
		        sessionIds.add(sessionId);
		    }
		}
		
		//	Change the visibility to unlocked
		unlockMessages(sessionIds);
		
		return queueMessage;
	}
	
	public Map<String, String> getAllFIFOMessages()
	{
		Map<String, String> queueMessage = new HashMap<String, String>();
		List<String> sessionIds = new ArrayList<String>();
	//	List<String> queueMessage = new ArrayList<String>();
		
		//	Get all the items from the queue.
		while(true)
		{
				
			ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(this.url);
				
			receiveMessageRequest.setMaxNumberOfMessages(SQS.maximumRetrievableMessages);
			receiveMessageRequest.setVisibilityTimeout(3600);
		        
			receiveMessageRequest.setMessageAttributeNames(new ArrayList<String>(Arrays.asList("All")));
		  
		    ReceiveMessageResult receiveMessageResult = sqs.receiveMessage(receiveMessageRequest);

		    /**
		     * Receives messages from the queue as list
		     *
		     * **/
		    List<Message> messages = receiveMessageResult.getMessages();
		    if(messages.size() == 0)
		    	break;
		        
		    for(Message message: messages)
		    {
		        	queueMessage.put(message.getMessageId(), message.getBody());
		    	//	queueMessage.add(message.getBody());
		    		String sessionId = message.getReceiptHandle();
		        	
		        sessionIds.add(sessionId);
		    }
		}
		
		//	Change the visibility to unlocked
		unlockMessages(sessionIds);
		
		return queueMessage;
	}
	
	/************************************************
	 * 	Get And Lock Message functions				*
	 ************************************************/
	
	/*	Get all messages from the queue
	 * 
	 */
	public Map<String, String> getAndLockMessage()
	{
		Map<String, String> queueMessages = new HashMap<String, String>();
		
		//	Get all the items from the queue.
		while(true)
		{
				
			ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(this.url);
				
			receiveMessageRequest.setMaxNumberOfMessages(1);
			receiveMessageRequest.setVisibilityTimeout(3600);
		        
			receiveMessageRequest.setMessageAttributeNames(new ArrayList<String>(Arrays.asList("All")));
		  
		    ReceiveMessageResult receiveMessageResult = sqs.receiveMessage(receiveMessageRequest);

		    /**
		     * Receives messages from the queue as list
		     *
		     * **/
		    List<Message> messages = receiveMessageResult.getMessages();
		    
		    if(messages.size() == 0)
		    	break;
		        
		    for(Message message: messages)
		    	queueMessages.put(message.getReceiptHandle(), message.getBody());	
		    
		}
		
		return queueMessages;
	}
	
	
	/*	Get and lock n message from the queue
	 * 
	 */
	public Map<String, String> getAndLockMessages(int n)
	{
		Map<String, String> queueMessages = new HashMap<String, String>();
		List<String> unclockMessages = new ArrayList<String>();
		int count = 0;
		
		//	Get all the items from the queue.
		while(true)
		{
				
			ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(this.url);
				
			receiveMessageRequest.setMaxNumberOfMessages((SQS.maximumRetrievableMessages > n) ? n:SQS.maximumRetrievableMessages);
			receiveMessageRequest.setVisibilityTimeout(3600);
		        
			receiveMessageRequest.setMessageAttributeNames(new ArrayList<String>(Arrays.asList("All")));
		  
		    ReceiveMessageResult receiveMessageResult = sqs.receiveMessage(receiveMessageRequest);

		    /**
		     * Receives messages from the queue as list
		     *
		     * **/
		    List<Message> messages = receiveMessageResult.getMessages();

		    if(messages.size() == 0)
		    	break;
		        
		    for(Message message: messages)
		    {
		    	queueMessages.put(message.getReceiptHandle(), message.getBody());
		    	if(count++ >= n) unclockMessages.add(message.getReceiptHandle());	
		    }
		    
		    if(count++ >= n) break;
		}
		
		unlockMessages(unclockMessages);
		return queueMessages;
	}
	
	/*	Get and lock all messages from the queue
	 * 
	 */
	public Map<String, String> getAndLockAllMessages()
	{
		Map<String, String> queueMessages = new HashMap<String, String>();
		
		//	Get all the items from the queue.
		while(true)
		{
				
			ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(this.url);
				
			receiveMessageRequest.setMaxNumberOfMessages(SQS.maximumRetrievableMessages);
			receiveMessageRequest.setVisibilityTimeout(3600);
		        
			receiveMessageRequest.setMessageAttributeNames(new ArrayList<String>(Arrays.asList("All")));
		  
		    ReceiveMessageResult receiveMessageResult = sqs.receiveMessage(receiveMessageRequest);

		    /**
		     * Receives messages from the queue as list
		     *
		     * **/
		    List<Message> messages = receiveMessageResult.getMessages();

		    if(messages.size() == 0)
		    	break;
		        
		    for(Message message: messages)
		    	queueMessages.put(message.getReceiptHandle(), message.getBody());	
		    
		}
		
		return queueMessages;
	}
	
	/************************************************
	 * 	Dequeue Messages							*
	 ************************************************/
	

	/*	
	 * 	Dequeue a message from the queue.
	 */
	public String dequeueMessage()
	{
		Entry<String, String> message = getAndLockMessage().entrySet().iterator().next();
		deleteMessage(message.getKey());
		
		return message.getValue();
	}
	
	/*	
	 * 	Get all messages from the queue
	 */
	public List<String> dequeueAllMessages()
	{
		List<String> queueMessage = new ArrayList<String>();
		
		//	Get all the items from the queue.
		while(true)
		{
				
			ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(this.url);
				
			receiveMessageRequest.setMaxNumberOfMessages(SQS.maximumRetrievableMessages);
			receiveMessageRequest.setVisibilityTimeout(3600);
		        
			receiveMessageRequest.setMessageAttributeNames(new ArrayList<String>(Arrays.asList("All")));
		  
		    ReceiveMessageResult receiveMessageResult = sqs.receiveMessage(receiveMessageRequest);

		    /**
		     * Receives messages from the queue as list
		     *
		     * **/
		    List<Message> messages = receiveMessageResult.getMessages();
		    if(messages.size() == 0)
		    	break;
		        
		    for(Message message: messages)
		    {
		        	
		    	queueMessage.add(message.getBody());
		    	deleteMessage(message.getReceiptHandle());
		    }
		}
		
		return queueMessage;
	}
	
	

	/************************************************
	 * 	Lock and Unlock Message functions			*
	 ************************************************/
	
	/*	Lock messages
	 * 
	 */
	public void lockMessages(String sessionId, int seconds)
	{
		sqs.changeMessageVisibility(new ChangeMessageVisibilityRequest(this.url, sessionId, seconds));
	}
	
	/*	Lock messages
	 * 
	 */
	public void lockMessages(List<String> sessionIds, int seconds)
	{
		for(String sessionId: sessionIds)	
			sqs.changeMessageVisibility(new ChangeMessageVisibilityRequest(this.url, sessionId, seconds));
	}
	
	/*	Unlock messages
	 * 
	 */
	public void unlockMessages(String sessionId)
	{
		sqs.changeMessageVisibility(new ChangeMessageVisibilityRequest(this.url, sessionId, 0));
	}
	
	/*	Lock messages
	 * 
	 */
	public void unlockMessages(List<String> sessionIds)
	{
		for(String sessionId: sessionIds)	
			sqs.changeMessageVisibility(new ChangeMessageVisibilityRequest(this.url, sessionId, 0));
	}
	
	
	/************************************************
	 * 	Counting functions							*
	 ************************************************/
	
	public int getCount()
	{
		return Integer.parseInt(sqs.getQueueAttributes(new GetQueueAttributesRequest(this.url).withAttributeNames("ApproximateNumberOfMessages")).getAttributes().get("ApproximateNumberOfMessages").toString());
	}
	
	public String getCountAsString()
	{
		return sqs.getQueueAttributes(new GetQueueAttributesRequest(this.url).withAttributeNames("ApproximateNumberOfMessages")).getAttributes().get("ApproximateNumberOfMessages").toString();
	}
	
	public GetQueueAttributesResult getQueueAttributes ()
	{
		return sqs.getQueueAttributes(new GetQueueAttributesRequest(this.url).withAttributeNames("All"));
	}
	
	/************************************************
	 * 	Dequeue functions							*
	 ************************************************/
	
	
	/************************************************
	 * 	Transfer functions							*
	 ************************************************/
	//	Move
	//	Copy
	
	/************************************************
	 * 	Find Queue URL functions					*
	 ************************************************/
	
	/*	Returns the queue URL.
	 * 
	 */
	public String findQueueUrl(String queueName)
	{
		for(String url: sqs.listQueues().getQueueUrls())
		{
			if(url.contains(queueName))	return url;
		}
		return "DoesNotExist";
	}
	
	/*	Returns and sets teh Queue URL.
	 * 
	 */
	public String findAndSetQueueUrl(String queueName)
	{
		for(String url: sqs.listQueues().getQueueUrls())
		{
			if(url.contains(queueName))	
			{
				this.url = url;
				return url;
			}
		}
		return "DoesNotExist";
	}
	
	/************************************************
	 * 	List all Queues								*
	 ************************************************/
	
	/*
	 * 	List all the queues currently present.
	 */
	public List<String> listQueues()
	{
		return sqs.listQueues().getQueueUrls();	
	}
	
	
	/************************************************
	 * 	Bulk Send Message functions					*
	 ************************************************/
	
	/************************************************
	 * 	Create New Queue							*
	 ************************************************/

	/*
	 * 	This function creates a new queue.
	 */
	public String createQueue(String queueName)
	{
		System.out.println("createQueue library");
		CreateQueueRequest createQueueRequest = new CreateQueueRequest();
		createQueueRequest.setQueueName(queueName);
		
		CreateQueueResult createQueueResponse = sqs.createQueue(createQueueRequest);
		System.out.println("createQueueResponse=" + createQueueResponse);
		
		String url = createQueueResponse.getQueueUrl();
		
		System.out.println("Queue URL:" + url);
		return url;
	}
	
	/************************************************
	 * 	Delete Existing Queue						*
	 ************************************************/
	
	/*
	 * 	This function deletes an existing queue.
	 */
	public void deleteQueue()
	{
		System.out.println("deleteQueue library");
		DeleteQueueRequest deleteQueueRequest = new DeleteQueueRequest();
		System.out.println("Url to be deleted:"+ url);
		deleteQueueRequest.setQueueUrl(url);
		
		DeleteQueueResult deleteQueueResult = sqs.deleteQueue(deleteQueueRequest);
		System.out.println("DeleteQueueResult=" + deleteQueueResult);	
	}
	
	/************************************************
	 * 	Delete Existing messages					*
	 ************************************************/
	
	/*
	 * 	Delete messages with the following session IDS.
	 */
	public void deleteMessage(String sessionId) 
	{
		DeleteMessageRequest request = new DeleteMessageRequest();
	    request.setQueueUrl(this.url);
	    request.setReceiptHandle(sessionId);
	    sqs.deleteMessage(request);
	}
	
	/*
	 * 	Delete messages with the following session IDS.
	 */
	public void deleteMessages(List<String> sessionIds) 
	{
		for(String sessionId : sessionIds)
		{
			deleteMessage(sessionId);
		}
	}
	
	/************************************************
	 * 	Move messages								*
	 ************************************************/
	
	/*
	 * 	Move all messages from 1 queue to another.
	 */
	public void moveMessages(boolean useMessageId, boolean useQueueName, String...strings)
	{
		int length = strings.length;
		
		for(int i = 0; i < length-1; i++)
		{
			if(useQueueName)	this.url = findAndSetQueueUrl(strings[i]);
			else	this.url = strings[i];
			
			List<String> messages = dequeueAllMessages();
			if(useQueueName)	this.url = findAndSetQueueUrl(strings[length -1]);
			else	this.url = strings[length - 1];
			
			for(String message : messages)
			{
				if(useMessageId)		sendMessageWithMessageId(message, UUID.randomUUID().toString());
				else	sendMessage(message);
			}
		}
	}
}