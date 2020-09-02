package org.yusuf.javabrain.controller;

import org.yusuf.javabrain.model.Item;
import org.yusuf.javabrain.service.ItemService;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("items")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ItemController
{
    private ItemService itemService = new ItemService();

    @PermitAll
    @GET
    public List<Item> getItems()
    {
        return itemService.getAllitems();
    }

    @POST
    public Item addItem(Item item)
    {
        return itemService.addItem(item);
    }

    @PUT
    @Path("/{itemId}")
    public Item updateItem(@PathParam("itemId") int id, Item item)
    {
        item.setId(id);
        return itemService.updateItem(item);
    }

    @DELETE
    @Path("/{itemId}")
    public Item deleteMessage(@PathParam("itemId") int id)
    {
        return itemService.removeItem(id);
    }

    @GET
    @Path("/{messageId}")
    public Item getMessage(@PathParam("messageId") int id)
    {
        return itemService.getItem(id);
    }
}