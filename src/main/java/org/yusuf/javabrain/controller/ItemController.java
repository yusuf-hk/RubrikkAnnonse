package org.yusuf.javabrain.controller;

import org.yusuf.javabrain.model.Item;
import org.yusuf.javabrain.model.User;
import org.yusuf.javabrain.security.AuthenticationService;
import org.yusuf.javabrain.security.ResponsePojo;
import org.yusuf.javabrain.service.ItemService;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("items")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ItemController
{
    private ItemService itemService = new ItemService();
    private AuthenticationService authenticationService = new AuthenticationService();

    @GET
    public List<Item> getItems()
    {
        return itemService.getAllitems();
    }

    @POST
    public Response addItem(@HeaderParam("authorization") String token, Item item)
    {
        User user = authenticationService.validateToken(token);
        if (user != null)
        {
            return Response.ok().entity(itemService.addItem(item)).build();
        }
        else
        {
            ResponsePojo responsePojo = new ResponsePojo();
            responsePojo.setError("Invalid token");
            return Response.ok().entity(responsePojo).build();
        }
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
    public Response deleteItem(@PathParam("itemId") int id)
    {
        return Response.ok().entity(itemService.removeItem(id)).build();
    }

    @GET
    @Path("/{itemId}")
    public Response getItem(@PathParam("itemId") int id)
    {
        return Response.ok().entity(itemService.getItem(id)).build();
    }

    @GET
    @Path("/{itemId/offer}")
    public Response offerItem(@HeaderParam("authorization") String token, @PathParam("itemId") int id)
    {
        User user = authenticationService.validateToken(token);
        if(user != null)
        {
            
        }
    }
}