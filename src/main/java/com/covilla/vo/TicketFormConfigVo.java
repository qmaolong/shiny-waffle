package com.covilla.vo;

import com.covilla.model.mongo.printer.TicketForm;

/**
 * Created by qmaolong on 2016/10/13.
 */
public class TicketFormConfigVo {
    private TicketForm kitchenForm;
    private TicketForm ticketForm;
    private TicketForm controlForm;

    public TicketForm getKitchenForm() {
        return kitchenForm;
    }

    public void setKitchenForm(TicketForm kitchenForm) {
        this.kitchenForm = kitchenForm;
    }

    public TicketForm getControlForm() {
        return controlForm;
    }

    public void setControlForm(TicketForm controlForm) {
        this.controlForm = controlForm;
    }

    public TicketForm getTicketForm() {
        return ticketForm;
    }

    public void setTicketForm(TicketForm ticketForm) {
        this.ticketForm = ticketForm;
    }
}
