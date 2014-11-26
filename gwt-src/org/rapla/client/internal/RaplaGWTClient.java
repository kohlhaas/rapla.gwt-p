package org.rapla.client.internal;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.ImageIcon;

import org.rapla.AppointmentFormaterImpl;
import org.rapla.components.util.Cancelable;
import org.rapla.components.util.Command;
import org.rapla.components.util.CommandScheduler;
import org.rapla.components.xmlbundle.I18nBundle;
import org.rapla.entities.domain.AppointmentFormater;
import org.rapla.facade.ClientFacade;
import org.rapla.facade.RaplaComponent;
import org.rapla.facade.internal.FacadeImpl;
import org.rapla.framework.RaplaDefaultContext;
import org.rapla.framework.RaplaException;
import org.rapla.framework.RaplaLocale;
import org.rapla.framework.logger.NullLogger;
import org.rapla.storage.StorageOperator;
import org.rapla.storage.dbrm.RemoteOperator;
import org.rapla.storage.dbrm.RemoteServer;
import org.rapla.storage.dbrm.RemoteStorage;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;

@Singleton
public class RaplaGWTClient {
    @Inject 
    RemoteServer remoteServer;
    @Inject 
    RemoteStorage remoteStorage;
    
    RemoteOperator remoteOperator;
    
    ClientFacade clientFacace;

    final GWTRaplaLocale raplaLocale;
	final RaplaDefaultContext context = new RaplaDefaultContext();
	//RemoteConnectionInfo connectionInfo = new RemoteConnectionInfo();
	public RaplaGWTClient() throws RaplaException {
		final Logger gwtLogger = Logger.getLogger("componentClass");
	    final org.rapla.framework.logger.Logger logger = new NullLogger();
		//final RaplaConfiguration config = new RaplaConfiguration("remote");
		raplaLocale = new GWTRaplaLocale();
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
			public Cancelable schedule(final Command command, long delay, final long period) {
			    if ( period > 0)
				{
			        RepeatingCommand cmd = new RepeatingCommand() {
				        
	                    @Override
	                    public boolean execute() {
	                        try {
	                            gwtLogger.info("Refreshing client with period " + period);
	                            command.execute();
	                        } catch (Exception e) {
	                            // TODO Auto-generated catch block
	                            e.printStackTrace();
	                        }
	                        return true;
	                    }
	                };
	                //Scheduler.get().scheduleEntry( cmd);
				    //Scheduler.get().scheduleFixedDelay(cmd, (int)delay);
					Scheduler.get().scheduleFixedPeriod(cmd, (int)period);
				}
				else
				{
				    ScheduledCommand entry = new ScheduledCommand() {
	                    
	                    @Override
	                    public void execute() {
	                        try {
	                            gwtLogger.info("Refreshing client without period ");
	                            command.execute();
	                        } catch (Exception e) {
	                            // TODO Auto-generated catch block
	                            e.printStackTrace();
	                        }
	                        
	                    }
	                };
	                Scheduler.get().scheduleEntry( entry);
				    //Scheduler.get().scheduleEntry(cmd);
					//Scheduler.get().scheduleFixedDelay(cmd, (int)delay);
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
	}
	
	@Inject
	private void init() throws RaplaException
	{      
	    org.rapla.framework.logger.Logger logger = context.lookup( org.rapla.framework.logger.Logger.class);
	    remoteOperator = new RemoteOperator(context, logger, null, remoteServer, remoteStorage);
	    context.put( StorageOperator.class, remoteOperator);
	    clientFacace = FacadeImpl.create(context, remoteOperator, logger);
	    context.put( ClientFacade.class, clientFacace );
	}
	
	public RaplaDefaultContext getContext() 
	{
	    return context;
	}
	
	public RemoteOperator getOperator()
	{
	    return remoteOperator;
	}
	
	public ClientFacade getFacade()
	{
        return clientFacace;
	}

    public RaplaLocale getRaplaLocale() {
        return raplaLocale;
    }
}
