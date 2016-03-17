package com.xebialabs.xldeploy.notifier;

import nl.javadude.t2bus.Subscribe;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.xebialabs.deployit.engine.spi.event.DeployitEventListener;
import com.xebialabs.deployit.engine.spi.event.TaskAbortedEvent;
import com.xebialabs.deployit.engine.spi.event.TaskArchivedEvent;
import com.xebialabs.deployit.engine.spi.event.TaskCancelledEvent;
import com.xebialabs.deployit.engine.spi.event.TaskScheduledEvent;
import com.xebialabs.deployit.engine.spi.event.TaskStartedEvent;
import com.xebialabs.deployit.engine.spi.event.TaskStoppedEvent;

@DeployitEventListener
public class XldBotNotifier {
	
	private static String BOT_URL = "http://localhost:8080";
	
	@Subscribe
	public void log(TaskStartedEvent event) throws UnirestException {
		postNotification(event.getTaskId(), "started");
	}

	@Subscribe
	public void log(TaskStoppedEvent event) throws UnirestException {
		postNotification(event.getTaskId(), "stopped");
	}

	@Subscribe
	public void log(TaskCancelledEvent event) throws UnirestException {
		postNotification(event.getTaskId(), "cancelled");
	}
	
	@Subscribe
	public void log(TaskAbortedEvent event) throws UnirestException {
		postNotification(event.getTaskId(), "aborted");
	}
	
	@Subscribe
	public void log(TaskArchivedEvent event) throws UnirestException {
		postNotification(event.getTaskId(), "archived");
	}
	
	@Subscribe
	public void log(TaskScheduledEvent event) throws UnirestException {
		postNotification(event.getTaskId(), "scheduled");
	}
	
	private void postNotification(String taskId, String status) throws UnirestException {
		HttpResponse<String> jsonResponse = Unirest.post(BOT_URL + "/task/" + taskId + "/" + status)
				  .header("Content-type", "application/json")
				  .asString();
		if (jsonResponse.getStatus() != 200) {
			System.out.println("Error invoking bot: " + jsonResponse.getStatus());
		}
	}
}
