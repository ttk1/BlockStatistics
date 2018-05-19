package net.ttk1.blockstatistics;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import io.ebean.EbeanServer;

public class PluginModule extends AbstractModule {
    private final BlockStatistics plugin;

    public PluginModule(BlockStatistics plugin) {
        this.plugin = plugin;
    }

    public Injector createInjector() {
        return Guice.createInjector(this);
    }

    @Override
    protected void configure(){
        bind(BlockStatistics.class).toInstance(plugin);
        bind(EbeanServer.class).toProvider(EbeanServerProvider.class).asEagerSingleton();
        bind(String.class).annotatedWith(Names.named("ebeanServerName")).toInstance(plugin.getDataFolder().getAbsolutePath()+"\\database");
    }
}
