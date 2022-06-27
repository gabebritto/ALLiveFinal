package regras_negocio;

import java.util.ArrayList;
import modelo.Convidado;
import modelo.Evento;
import modelo.Participante;
import repositorio.Repositorio;


/**********************************
 * Grupo de alunos: 
 * Erick Fonseca
 * Gabriel Britto
 **********************************/

public class Fachada {
	private Fachada() {}		
	private static Repositorio repositorio = new Repositorio();	

	public static ArrayList<Participante> listarParticipantes(String texto) {
		ArrayList<Participante> lista = repositorio.getParticipantes();
		ArrayList<Participante> selecionados = new ArrayList<>();
		
		for(Participante p : lista )
			if(p.getNome().contains(texto))
				selecionados.add(p);
			
		return selecionados;
	}
	
	public static ArrayList<Evento> listarEventos() 	{
		return repositorio.getEventos();
	}
	
	public static Evento localizarEvento(int id) 	{
		 return repositorio.localizarEvento(id);
	}
	public static Evento localizarEvento(String data) 	{
		return repositorio.localizarEvento(data);
	}
	
	public static Participante localizarParticipante(String nome) {
		return repositorio.localizarParticipante(nome);
	}
	
	public static Participante criarParticipante(String email, String nome, int idade) throws Exception {
		email = email.trim();
		nome = nome.trim();
		Participante participante;
		
		//localizar participante no repositorio, usando o nome 
		participante = repositorio.localizarParticipante(nome);
		if (participante!=null)
			throw new Exception("N�o criou participante: " + nome + " ja cadastrado(a)");

		//criar objeto Participante
		if (idade < 0){
			throw new Exception("N�o criou participante: " + "Idade "+idade+" invalida");
		}
		participante = new Participante (email, nome, idade);

		//adicionar participante no reposit�rio
		repositorio.adicionar(participante);
		//gravar reposit�rio
		repositorio.salvar();
		//retornar objeto criado
		return participante;	
	}	

	public static Convidado criarConvidado(String email,String nome, int idade, String empresa) throws Exception {
		email = email.trim();
		nome = nome.trim();
		empresa = empresa.trim();
		Convidado convidado;

		convidado = (Convidado) repositorio.localizarParticipante(nome);
		if (convidado!=null) {
			throw new Exception("N�o criou convidado: " + nome + " ja cadastrado(a)");
		}
		if (idade < 1){
			throw new Exception("N�o criou convidado: " + "Idade: " + idade + "inv�lida");
		}
		//criar objeto Convidado
		convidado = new Convidado(email, nome, idade, empresa);
		//adicionar convidado no reposit�rio
		repositorio.adicionar(convidado);
		//gravar reposit�rio
		repositorio.salvar();
		//retornar objeto criado
		return convidado;
	}
	
	public static Evento criarEvento (String data, String descricao, double preco) throws Exception {
		data = data.trim();
		descricao = descricao.trim();
		Evento evento;
		
		//localizar Evento no repositorio, usando a data 
		evento = repositorio.localizarEvento(data);
		if (evento != null) {
			throw new Exception("Evento n�o criado: "+ data + "J� reservada");
		}
		//gerar id no repositorio
		int id = repositorio.gerarId();
		if (preco < 0){
			throw new Exception("Evento n�o criado: " + preco + "Menor que zero");
		}
		//criar evento
		evento =  new Evento(id,descricao, data, preco);
		//adicionar evento no reposit�rio
		repositorio.adicionar(evento);
		//gravar reposit�rio
		repositorio.salvar();
		//retornar objeto criado
		return evento;
	}

	public static void 	adicionarParticipanteEvento(String nome, int id) throws Exception {
		nome = nome.trim();

		//localizar participante no repositorio, usando o nome 
		Participante p = repositorio.localizarParticipante(nome);
		if(p == null) 
			throw new Exception("N�o adicionou participante:  " + nome + " inexistente");


		//localizar evento no repositorio, usando id 
		Evento ev = repositorio.localizarEvento(id);
		if(ev == null) 
			throw new Exception("N�o adicionou participante: evento " + id + " inexistente");


		//localizar o participante no evento, usando o nome
		Participante paux = ev.localizar(nome);
		if(paux != null) 
			throw new Exception("N�o adicionou participante: " + nome + " j� participa do evento " + id);

		//adicionar o participante ao evento
		ev.adicionar(p);
		//adicionar o evento ao participante
		p.adicionar(ev);
		//gravar reposit�rio
		repositorio.salvar();
	}

	public static void 	removerParticipanteEvento(String nome, int id) throws Exception {
		nome = nome.trim();

		//localizar participante no repositorio, usando o nome 
		Participante p = repositorio.localizarParticipante(nome);
		if (p == null)
			throw new Exception("N�o removeu participante: "+ nome +"inexistente");
		//localizar evento no repositorio, usando id 
		Evento ev = repositorio.localizarEvento(id);
		if (ev == null){
			throw new Exception("N�o removeu participante: "+id+"inexistente");
		}
		//localizar o participante no evento, usando o nome
		Participante localizar = ev.localizar(p.getNome());
		if (localizar != null){
			throw new Exception("N�o removeu participante: "+nome+"N�o participa do evento.");
		}
		//remover o participante do evento
		ev.remover(p);
		//remover o evento do participante
		p.remover(ev);
		//gravar reposit�rio
		repositorio.salvar();
	}

	public static void apagarEvento(String data) throws Exception	{
		//localizar evento no repositorio, usando id 
		Evento ev = repositorio.localizarEvento(data);
		if (ev == null)
			throw new Exception("N�o deletou evento: " + data + " inexistente");

		//Remover todos os participantes deste evento
		for(Participante p : ev.getParticipantes()) {
			p.remover(ev);
		}
		ev.getParticipantes().clear();
		
		//remover evento do reposit�rio
		repositorio.remover(ev);
		//gravar reposit�rio
		repositorio.salvar();
	}

	public static void adiarEvento(String data, String novadata) throws Exception	{
		//localizar evento no repositorio, usando data 
		Evento ev = repositorio.localizarEvento(data);
		if (ev == null){
			throw new Exception("N�o adiou evento: " + data + " inexistente");
		}
		//localizar evento no repositorio, usando novadata
		Evento dataOcupada = repositorio.localizarEvento(novadata);
		if (dataOcupada != null)
			throw new Exception("N�o adiou evento: "+novadata+ " Ocupada");
		//alterar a data do evento
		ev.setData(novadata);
		//gravar reposit�rio
		repositorio.salvar();
	}
	
	public static void 	apagarParticipante(String nome) throws Exception {
		nome = nome.trim();

		//localizar participante no repositorio, usando o nome 
		Participante p = repositorio.localizarParticipante(nome);
		if (p == null){
			throw new Exception("N�o apagou participante: "+nome+" inexistente");
		}
		//participante nao pode ser deletado caso participe de algum evento
		int qEventos = p.getEventos().size();
		if (qEventos > 0){
			throw new Exception("N�o apagou participante: "+nome+ " ainda participa de eventos");
		}
		//remover o participante do repositorio
		repositorio.remover(p);
		//gravar reposit�rio
		repositorio.salvar();
	}

}
