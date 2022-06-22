package iu_console;
import modelo.Convidado;
import modelo.Evento;
import modelo.Participante;
import regras_negocio.Fachada;

public class AplicacaoConsole1 {

	public AplicacaoConsole1() {
		try {
			System.out.println("\n---------listagem de participantes (arquivo)-----");
			for(Participante p : Fachada.listarParticipantes("")) 
				System.out.println(p);

			System.out.println("\n---------listagem de eventos (arquivo) ----");
			for(Evento e : Fachada.listarEventos()) 
				System.out.println(e);
		} catch (Exception e) {
			System.out.println("--->"+e.getMessage());
		}	



		try {
			//------ FESTA -------------
			Evento festa = Fachada.criarEvento("24/06/2022","festa de sao joao",100.0);
			System.out.println("\ncriou festa "+festa);

			Fachada.adiarEvento("24/06/2022", "25/06/2022");
			System.out.println("adiou festa de sao joao "+festa.getData());

			System.out.println("criar participantes da festa");
			Participante zezinho = 		Fachada.criarParticipante("zezinho@gmail.com", "zezinho",  150);
			Participante zezao = 		Fachada.criarParticipante("zezao@gmail.com", "zezao",  30);
			Convidado padre = 		Fachada.criarConvidado("padre@gmail.com", "padre",  70, "igreja");
			Convidado diretor = 	Fachada.criarConvidado("diretor@gmail.com", "diretor",  50, "ifpb");

			System.out.println("adicionar participantes da festa");
			Fachada.adicionarParticipanteEvento("zezinho", 4);
			Fachada.adicionarParticipanteEvento("zezao", 4);
			Fachada.adicionarParticipanteEvento("padre", 4);
			Fachada.adicionarParticipanteEvento("diretor", 4);

			Fachada.removerParticipanteEvento("diretor", 4);
			System.out.println("removeu diretor da festa "+ diretor);

			Fachada.apagarParticipante("diretor");
			System.out.println("apagou diretor do sistema ");

			//------ SHOW -------------
			Evento show = Fachada.criarEvento("01/07/2022","show da elba",200.0);
			System.out.println("\ncriou show "+show);

			System.out.println("adicionar participantes do show");
			Fachada.adicionarParticipanteEvento("zezinho", 5);
			Fachada.adicionarParticipanteEvento("zezao", 5);

			Fachada.adiarEvento("01/07/2022", "02/07/2022");
			System.out.println("adiou show "+festa.getData());

			Fachada.apagarEvento("02/07/2022");
			System.out.println("apagou show");

		} catch (Exception e) {
			System.out.println("--->"+e.getMessage());
		}		


		try {
			System.out.println("\n---------listagem de participantes final-----");
			for(Participante p : Fachada.listarParticipantes("")) 
				System.out.println(p);

			System.out.println("\n---------listagem de eventos final");
			for(Evento e : Fachada.listarEventos()) 
				System.out.println(e);
		} catch (Exception e) {
			System.out.println("--->"+e.getMessage());
		}	

		//apagar  eventos do teste
		try {Fachada.apagarEvento("24/06/2022");}	catch (Exception e) {}		
		try {Fachada.apagarEvento("25/06/2022");}	catch (Exception e) {}		
		try {Fachada.apagarEvento("01/07/2022");}	catch (Exception e) {}		
		try {Fachada.apagarEvento("02/07/2022");}	catch (Exception e) {}		
		//apagar novos participantes do teste
		try {Fachada.apagarParticipante("zezinho");}catch (Exception e) {}		
		try {Fachada.apagarParticipante("zezao");}	catch (Exception e) {}		
		try {Fachada.apagarParticipante("padre");}	catch (Exception e) {}		
		try {Fachada.apagarParticipante("diretor");}catch (Exception e) {}		
	}

	public static void main (String[] args) 
	{
		AplicacaoConsole1 aplicacaoConsole1 = new AplicacaoConsole1();
	}
}


