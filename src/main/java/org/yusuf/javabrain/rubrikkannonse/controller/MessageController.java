package org.yusuf.javabrain.rubrikkannonse.controller;

import org.yusuf.javabrain.rubrikkannonse.model.Message;
import org.yusuf.javabrain.rubrikkannonse.service.MessageService;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("messages")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MessageController
{
    private MessageService messageService = new MessageService();

    @RolesAllowed("Customer")
    @GET
    public List<Message> getMessages()
    {
        return messageService.getAllMessages();
    }

    @POST
    public Message addMessage(Message message)
    {
        return messageService.addMessage(message);
    }

    @PUT
    @Path("/{messageId}")
    public Message updateMessage(@PathParam("messageId") int id, Message message)
    {
        message.setId(id);
        return messageService.updateMessage(message);
    }

    @DELETE
    @Path("/{messageId}")
    public Message deleteMessage(@PathParam("messageId") int id)
    {
        return messageService.removeMessage(id);
    }

    @GET
    @Path("/{messageId}")
    public Message getMessage(@PathParam("messageId") int messageId)
    {
        return messageService.getMessage(messageId);
    }
}