package org.rapla.client.test;

import java.util.Locale;
import java.util.MissingResourceException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.ImageIcon;

import org.rapla.AppointmentFormaterImpl;
import org.rapla.components.util.Cancelable;
import org.rapla.components.util.Command;
import org.rapla.components.util.CommandScheduler;
import org.rapla.components.xmlbundle.I18nBundle;
import org.rapla.entities.domain.AppointmentFormater;
import org.rapla.facade.RaplaComponent;
import org.rapla.framework.RaplaDefaultContext;
import org.rapla.framework.RaplaException;
import org.rapla.framework.RaplaLocale;
import org.rapla.framework.logger.NullLogger;
import org.rapla.storage.dbrm.RemoteOperator;
import org.rapla.storage.dbrm.RemoteServer;
import org.rapla.storage.dbrm.RemoteStorage;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;

@Singleton
public class RaplaGWTClient {
    @Inject 
    RemoteServer remoteServer;
    @Inject 
    RemoteStorage remoteStorage;
    
    RemoteOperator remoteOperator;
    
	final RaplaDefaultContext context = new RaplaDefaultContext();
	//RemoteConnectionInfo connectionInfo = new RemoteConnectionInfo();
	public RaplaGWTClient() throws RaplaException {
		final org.rapla.framework.logger.Logger logger = new NullLogger();
		//final RaplaConfiguration config = new RaplaConfiguration("remote");
		final GWTRaplaLocale raplaLocale = new GWTRaplaLocale();
		I18nBundle i18n = new I18nBundle() {
			
			@Override
			public String getString(String key) throws MissingResourceException {
				return key;
			}
			
			@Override
			public Locale getLocale() {
				return null;
			}
			
			@Override
			public String getLang() {
				return "en";
			}
			
			@Override
			public ImageIcon getIcon(String key) throws MissingResourceException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String format(String key, Object... obj)
					throws MissingResourceException {
				return key;
			}
			
			@Override
			public String format(String key, Object obj1, Object obj2)
					throws MissingResourceException {
				return key;
			}
			
			@Override
			public String format(String key, Object obj1)
					throws MissingResourceException {
				return key;
			}

			@Override
			public String getString(String key, Locale locale) {
				return key;
			}
		};
		final CommandScheduler commandQueue = new CommandScheduler() {
			
			@Override
			public Cancelable schedule(final Command command, long delay, long period) {
				RepeatingCommand cmd = new RepeatingCommand() {
	
				    @Override
				    public boolean execute() {
				        try {
							//command.execute();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				        return true;
				    }
				};
				if ( period > 0)
				{
					Scheduler.get().scheduleFixedPeriod(cmd, (int)period);
				}
				else
				{
					Scheduler.get().scheduleFixedDelay(cmd, (int)delay);
				}
				
				return new Cancelable() {
					
					public void cancel() {
					}
				};
			}
			
			@Override
			public Cancelable schedule(Command command, long delay) {
				return schedule(command, delay, -1);
			}
			
		};
		context.put( org.rapla.framework.logger.Logger.class, logger);
		context.put( RaplaLocale.class, raplaLocale);
		context.put( CommandScheduler.class, commandQueue);
		context.put( RaplaComponent.RAPLA_RESOURCES, i18n);
        AppointmentFormater appointmentFormater = new AppointmentFormaterImpl(context);
        context.put( AppointmentFormater.class, appointmentFormater);
		//final RemoteOperator remoteOperator = new RemoteOperator(context, logger, config, remoteServer, remoteStorage);
//		FacadeImpl facade = FacadeImpl.create(context, remoteOperator, logger);
//		context.put(ClientFacade.class, facade);
	}
	
	@Inject
	private void init() throws RaplaException
	{      
	    org.rapla.framework.logger.Logger logger = context.lookup( org.rapla.framework.logger.Logger.class);
	    remoteOperator = new RemoteOperator(context, logger, null, remoteServer, remoteStorage);
	}
	
	public RaplaDefaultContext getContext() 
	{
		return context;
	}
	
	public RemoteOperator getOperator()
	{
	    return remoteOperator;
	}

}
