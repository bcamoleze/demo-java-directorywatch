package br.com.camoleze.demo;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Main {
	
	public static void main(String[] args) {
		
		System.out.println("Watching the directory...");
		
		try (WatchService service = FileSystems.getDefault().newWatchService() ) {
			
			Map<WatchKey, Path> keyMap = new HashMap<>();
			
			// passar o caminho do diretorio que for ser assistido 
			Path path = Paths.get("arquivos");
			
			keyMap.put(path.register(service, 
					   StandardWatchEventKinds.ENTRY_CREATE,
					   StandardWatchEventKinds.ENTRY_DELETE,
					   StandardWatchEventKinds.ENTRY_MODIFY), 
					   path);
			
			WatchKey watchKey;
			
			do {
				
				watchKey = service.take();
				Path eventDir = keyMap.get(watchKey);
				
				for(WatchEvent<?> event : watchKey.pollEvents()) {
					
					WatchEvent.Kind<?> kind = event.kind();
					Path evenPath = (Path) event.context();
							
					// pegar a data e tratar
					SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");    
					Date data = new Date();
					
					System.out.println(fmt.format(data) + " - " +									   
									   eventDir + ": " + kind + ": " + evenPath);
					
				}
				
				
			} while(watchKey.reset());
			
		} catch (RuntimeException ex) {
			
			ex.printStackTrace();
		
		} catch (IOException ex) {
		
			ex.printStackTrace();
		
		} catch (InterruptedException ex) {
		
			ex.printStackTrace();
	
		}
		
	}

}
