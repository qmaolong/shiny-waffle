package com.covilla.model.mongo.printer;

import com.covilla.model.mongo.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qmaolong on 2016/10/14.
 */
public class ShopForm extends BaseModel {
    private List<TicketForm> ticketForms = new ArrayList<TicketForm>();
    private List<TicketForm> kitchenForms = new ArrayList<TicketForm>();
    private List<TicketForm> controlForms = new ArrayList<TicketForm>();

    public List<TicketForm> getTicketForms() {
        return ticketForms;
    }

    public void setTicketForms(List<TicketForm> ticketForms) {
        this.ticketForms = ticketForms;
    }

    public List<TicketForm> getKitchenForms() {
        return kitchenForms;
    }

    public void setKitchenForms(List<TicketForm> kitchenForms) {
        this.kitchenForms = kitchenForms;
    }

    public List<TicketForm> getControlForms() {
        return controlForms;
    }

    public void setControlForms(List<TicketForm> controlForms) {
        this.controlForms = controlForms;
    }
}
