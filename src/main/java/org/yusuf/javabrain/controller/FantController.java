package org.yusuf.javabrain.controller;

import org.yusuf.javabrain.model.Item;
import org.yusuf.javabrain.model.User;
import org.yusuf.javabrain.security.AuthenticationService;
import org.yusuf.javabrain.security.ResponsePojo;
import org.yusuf.javabrain.service.ItemService;
import org.yusuf.javabrain.service.TransactionService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("items")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FantController
{
    private TransactionService transactionService = new TransactionService();
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
            return Response.ok().entity(itemService.addItem(item, user.getId())).build();
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
    public Response updateItem(@HeaderParam("authorization") String token,
                           @PathParam("itemId") int id,
                           Item item)
    {
        User user = authenticationService.validateToken(token);
        if(user != null)
        {
            if (user.getId() != id)
            {
                item.setId(id);
                itemService.updateItem(item);
                return Response.ok().entity("You have successfully changed your item!").build();
            }
            else return Response.ok().entity("Error, You can only change your own items").build();
        }
        else return Response.ok().entity("Error, invalid token").build();
    }

    @DELETE
    @Path("/{itemId}")
    public Response deleteItem(@HeaderParam("authorization") String token, @PathParam("itemId") int id)
    {
        User user = authenticationService.validateToken(token);
        if(user != null) return Response.ok().entity(itemService.removeItem(id)).build();
        else return Response.ok().entity("Error, invalid token").build() ;
    }

    @GET
    @Path("/{itemId}")
    public Response getItem(@PathParam("itemId") int id)
    {
        return Response.ok().entity(itemService.getItem(id)).build();
    }

    @GET
    @Path("/{itemId}/buy")
    public Response buyItem(@HeaderParam("authorization") String token, @PathParam("itemId") int id)
    {
        User user = authenticationService.validateToken(token);
        if(user != null)
        {
            boolean result = transactionService.buyItem(user.getId(), id);
            if(!result) return Response.ok().entity("Error, something went wrong").build();
            else return Response.ok().entity("You have successfully bought the item!").build();
        }
        else
        {
            ResponsePojo responsePojo = new ResponsePojo();
            responsePojo.setError("invalid token");
            return Response.ok().entity(responsePojo).build();
        }
    }
}