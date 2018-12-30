
package com.ec.resourse;

import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class Resources {

    // Expose an entity manager using the resource producer pattern
    @PersistenceContext
    @Produces
    private EntityManager em;

    @Produces
    public Logger getLogger(InjectionPoint input) {
        String a = input.getMember().getDeclaringClass().getName();
        return Logger.getLogger(a);
    }

    @Produces
    @RequestScoped
    public FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

}
