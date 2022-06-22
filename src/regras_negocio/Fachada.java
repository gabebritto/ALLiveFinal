package regras_negocio;

import java.util.ArrayList;
import modelo.Convidado;
import modelo.Evento;
import modelo.Participante;
import repositorio.Repositorio;


/**********************************
 * Grupo de alunos: 
 * ?????
 * ????:
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
			throw new Exception("Não criou participante: " + nome + " ja cadastrado(a)");

		//criar objeto Participante 
		participante = new Participante (email, nome, idade);

		//adicionar participante no repositório
		repositorio.adicionar(participante);
		//gravar repositório
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
			throw new Exception("Não criou convidado: " + nome + " ja cadastrado(a)");
		}
		//criar objeto Convidado
		convidado = new Convidado(email, nome, idade, empresa);
		//adicionar convidado no repositório
		repositorio.adicionar(convidado);
		//gravar repositório
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
			throw new Exception("Evento não criado: "+ data + "Já reservada");
		}
		//gerar id no repositorio
		int id = repositorio.gerarId();
		
		//criar evento
		evento =  new Evento(id,descricao, data, preco);
		//adicionar evento no repositório
		repositorio.adicionar(evento);
		//gravar repositório
		repositorio.salvar();
		//retornar objeto criado
		return evento;
	}

	public static void 	adicionarParticipanteEvento(String nome, int id) throws Exception {
		nome = nome.trim();

		//localizar participante no repositorio, usando o nome 
		Participante p = repositorio.localizarParticipante(nome);
		if(p == null) 
			throw new Exception("Não adicionou participante:  " + nome + " inexistente");


		//localizar evento no repositorio, usando id 
		Evento ev = repositorio.localizarEvento(id);
		if(ev == null) 
			throw new Exception("Não adicionou participante: evento " + id + " inexistente");


		//localizar o participante no evento, usando o nome
		Participante paux = ev.localizar(nome);
		if(paux != null) 
			throw new Exception("Não adicionou participante: " + nome + " já participa do evento " + id);

		//adicionar o participante ao evento
		ev.adicionar(p);
		//adicionar o evento ao participante
		p.adicionar(ev);
		//gravar repositório
		repositorio.salvar();
	}

	public static void 	removerParticipanteEvento(String nome, int id) throws Exception {
		nome = nome.trim();

		//localizar participante no repositorio, usando o nome 
		Participante p = repositorio.localizarParticipante(nome);
		if (p == null)
			throw new Exception("Não removeu participante: "+ nome +"inexistente");
		//localizar evento no repositorio, usando id 
		Evento ev = repositorio.localizarEvento(id);
		if (ev == null){
			throw new Exception("Não removeu participante: "+id+"inexistente");
		}
		//localizar o participante no evento, usando o nome
		Participante localizar = ev.localizar(p.getNome());
		if (localizar != null){
			throw new Exception("Não removeu participante: "+nome+"Não participa do evento.");
		}
		//remover o participante do evento
		ev.remover(p);
		//remover o evento do participante
		p.remover(ev);
		//gravar repositório
		repositorio.salvar();
	}

	public static void apagarEvento(String data) throws Exception	{
		//localizar evento no repositorio, usando id 
		Evento ev = repositorio.localizarEvento(data);
		if (ev == null)
			throw new Exception("Não deletou evento: " + data + " inexistente");

		//Remover todos os participantes deste evento
		for(Participante p : ev.getParticipantes()) {
			p.remover(ev);
		}
		ev.getParticipantes().clear();
		
		//remover evento do repositório
		repositorio.remover(ev);
		//gravar repositório
		repositorio.salvar();
	}

	public static void adiarEvento(String data, String novadata) throws Exception	{
		//localizar evento no repositorio, usando data 
		Evento ev = repositorio.localizarEvento(data);
		if (ev == null){
			throw new Exception("Não adiou evento: " + data + " inexistente");
		}
		//localizar evento no repositorio, usando novadata
		Evento dataOcupada = repositorio.localizarEvento(novadata);
		if (dataOcupada != null)
			throw new Exception("Não adiou evento: "+novadata+ " Ocupada");
		//alterar a data do evento
		ev.setData(novadata);
		//gravar repositório
		repositorio.salvar();
	}
	
	public static void 	apagarParticipante(String nome) throws Exception {
		nome = nome.trim();

		//localizar participante no repositorio, usando o nome 
		Participante p = repositorio.localizarParticipante(nome);
		if (p == null){
			throw new Exception("Não apagou participante: "+nome+" inexistente");
		}
		//participante nao pode ser deletado caso participe de algum evento
		int qEventos = p.getEventos().size();
		if (qEventos > 0){
			throw new Exception("Não apagou participante: "+nome+ " ainda participa de eventos");
		}
		//remover o participante do repositorio
		repositorio.remover(p);
		//gravar repositório
		repositorio.salvar();
	}

}
