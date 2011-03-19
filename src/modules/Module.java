package modules;

import utils.video.processors.Data;

public abstract class Module {

	protected Cargo gui_cargo;
	protected Cargo file_cargo;
	protected ModuleConfigs configs;
	protected String name;

	protected Data[] data;

	public Cargo getGUICargo() {
		return gui_cargo;
	}
	
	public Cargo getFileCargo()
	{
		return file_cargo;
	}
	
	public abstract void initialize();
	
	public String getName()
	{
		return name;
	}
	
	public abstract void deInitialize();

	public abstract void updateConfigs(ModuleConfigs config);

	public abstract void process();

	public abstract void updateGUICargoData();
	public abstract void updateFileCargoData();

	public abstract void updateDataObject(Data data);

	public Module(String name,ModuleConfigs config)
	{
		this.name=name;
		configs=config;
	}

	/*	public void addModule(Modulable module)
	{
		Modulable[] tmp=modules;
		int len=tmp.length;
		modules=new Modulable[len+1];
		System.arraycopy(tmp, 0, modules, 0,len);

		modules[len]=module;
		tmp=null;
	}

	public void deleteModule(Modulable module)
	{
		Modulable[] tmp=new Modulable[modules.length-1];
		int len=tmp.length;

		for(int i=0, j=0;i<len+1;i++)
			if(modules[i]!=module)
			{
				tmp[j]=modules[i];
				j++;
			}

		modules=tmp;
		tmp=null;
	}*/

	/*	public void distributeDataOnModules()
	{
		for(Modulable m: modules)
			m.process(data);
	}*/

}