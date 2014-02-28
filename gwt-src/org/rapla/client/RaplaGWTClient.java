package org.rapla.client;

import java.util.Date;
import java.util.Locale;
import java.util.MissingResourceException;

import javax.swing.ImageIcon;

import org.rapla.AppointmentFormaterImpl;
import org.rapla.components.util.Cancelable;
import org.rapla.components.util.Command;
import org.rapla.components.util.CommandScheduler;
import org.rapla.components.util.xml.RaplaNonValidatedInput;
import org.rapla.components.xmlbundle.I18nBundle;
import org.rapla.entities.RaplaType;
import org.rapla.entities.configuration.RaplaConfiguration;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.AppointmentFormater;
import org.rapla.facade.ClientFacade;
import org.rapla.facade.RaplaComponent;
import org.rapla.facade.internal.FacadeImpl;
import org.rapla.framework.RaplaDefaultContext;
import org.rapla.framework.RaplaException;
import org.rapla.framework.RaplaLocale;
import org.rapla.framework.SimpleProvider;
import org.rapla.framework.logger.NullLogger;
import org.rapla.storage.UpdateEvent;
import org.rapla.storage.dbrm.ConnectorFactory;
import org.rapla.storage.dbrm.EntityList;
import org.rapla.storage.dbrm.RemoteMethodCaller;
import org.rapla.storage.dbrm.RemoteOperator;
import org.rapla.storage.dbrm.RemoteServer;
import org.rapla.storage.dbrm.RemoteStorage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;

public class RaplaGWTClient {

	final RaplaDefaultContext context = new RaplaDefaultContext();
	
	public RaplaGWTClient() throws RaplaException {
		final org.rapla.framework.logger.Logger logger = new NullLogger();
		final RaplaConfiguration config = new RaplaConfiguration("remote");
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
			public String format(String key, Object[] obj)
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
		RaplaNonValidatedInput raplaParser = new RaplaXMLSAXParser();
		context.put( org.rapla.framework.logger.Logger.class, logger);
		context.put( RaplaLocale.class, raplaLocale);
		context.put( RaplaNonValidatedInput.class, raplaParser);
		context.put( ConnectorFactory.class, new GWTConnectorFactory("@doc.version@"));
		context.put( CommandScheduler.class, commandQueue);
		context.put( RaplaComponent.RAPLA_RESOURCES, i18n);
		final SimpleProvider<RemoteMethodCaller> callerProvider = new SimpleProvider<RemoteMethodCaller>();
		final RemoteServer remoteServer = new RemoteServer() {
			public Object call(String methodName,Class<?>[] parameterTypes, Class<?> returnType ,Object[] args) throws RaplaException
			{
				RemoteMethodCaller remoteMethodCaller = callerProvider.get();
				return remoteMethodCaller.call(RemoteServer.class, methodName, parameterTypes, returnType, args);
			}
			
			@Override
			public void logout() throws RaplaException {
				call("logout", null, null, null);
			}
			
			@Override
			public String login(String username, String password, String connectAs)
					throws RaplaException {
				String result = (String) call("login", new Class[] {String.class, String.class, String.class}, null, new Object[] {username, password, connectAs});
				return result;
			}
			
		};
		final RemoteStorage remoteStorage = GWT.create(RemoteStorage.class);
		final RemoteOperator remoteOperator = new RemoteOperator(context, logger, config, remoteServer, remoteStorage);
		callerProvider.setValue( remoteOperator);
		FacadeImpl facade = new FacadeImpl(context, remoteOperator, logger);
		context.put(ClientFacade.class, facade);
		AppointmentFormater appointmentFormater = new AppointmentFormaterImpl(context);
		context.put( AppointmentFormater.class, appointmentFormater);
	}
	
	public RaplaDefaultContext getContext() 
	{
		return context;
	}

}
