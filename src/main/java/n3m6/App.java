package n3m6;

import java.sql.SQLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class App {

	public static void main(String[] args) {

		// iniciando o servidor TCP do H2
		try {
			org.h2.tools.Server.createTcpServer().start();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("************** FALHA AO INICIAR BANCO DE DADOS **************");
		}

		SpringApplication.run(App.class);
	}

}
