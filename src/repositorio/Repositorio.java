/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * POO
 * Prof. Fausto Maranhão Ayres
 **********************************/
package repositorio;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

import modelo.Convidado;
import modelo.Evento;
import modelo.Participante;

public class Repositorio {
	private ArrayList<Participante> participantes = new ArrayList<>();
	private ArrayList<Evento> eventos = new ArrayList<>(); 

	public Repositorio() {
		carregar();
	}
	public void adicionar(Participante p)	{
		participantes.add(p);
	}

	public void remover(Participante p)	{
		participantes.remove(p);
	}

	public Participante localizarParticipante(String nome)	{
		for(Participante p: participantes)
			if(p.getNome().equals(nome))
				return p;
		return null;
	}

	public void adicionar(Evento e)	{
		eventos.add(e);
	}
	public void remover(Evento e)	{
		eventos.remove(e);
	}

	public Evento localizarEvento(int id)	{
		for(Evento e : eventos)
			if(e.getId() == id)
				return e;
		return null;
	}
	public Evento localizarEvento(String data)	{
		for(Evento e : eventos)
			if(e.getData().equals(data))
				return e;
		return null;
	}

	public ArrayList<Participante> getParticipantes() 	{
		return participantes;
	}
	public ArrayList<Evento> getEventos() 	{
		return eventos;
	}

	public int getTotalParticipante()	{
		return participantes.size();
	}

	public int getTotalEventos()	{
		return eventos.size();
	}

	public int gerarId() {
		int idUltimo = eventos.get(getTotalEventos()-1).getId();
		return idUltimo + 1;
	}
	public void carregar()  	{
		// carregar para o repositorio os objetos salvos nos arquivos csv
		String linha;	
		String[] partes;	
		String tipo,nome, email, empresa, idade, data, descricao, id, preco, listaId;
		Evento ev;
		Participante p;

		Scanner arquivo1=null;
		try	{
			File f = new File( new File(".\\eventos.csv").getCanonicalPath() )  ;
			arquivo1 = new Scanner(f);	 //  pasta do projeto
			while(arquivo1.hasNextLine()) 	{
				linha = arquivo1.nextLine().trim();		
				partes = linha.split(";");	
				//System.out.println(Arrays.toString(partes));
				id = partes[0];
				data = partes[1];
				descricao = partes[2];
				preco = partes[3];
				ev = new Evento(Integer.parseInt(id), descricao, data, Double.parseDouble(preco));
				this.adicionar(ev);
			} 
			arquivo1.close();
		}
		catch(Exception ex)		{
			throw new RuntimeException("leitura arquivo de eventos:"+ex.getMessage());
		}

		Scanner arquivo2=null;
		try	{
			File f = new File( new File(".\\participantes.csv").getCanonicalPath())  ;
			arquivo2 = new Scanner(f);	 //  pasta do projeto
			while(arquivo2.hasNextLine()) 	{
				linha = arquivo2.nextLine().trim();	
				partes = linha.split(";");
				//System.out.println(Arrays.toString(partes));
				tipo = partes[0];
				email = partes[1];
				nome = partes[2];
				idade = partes[3];
				listaId="";
				if(tipo.equals("PARTICIPANTE")) {
					p = new Participante(email,nome,Integer.parseInt(idade));
					this.adicionar(p);
					if(partes.length>4)
						listaId = partes[4];		//lista de id separados por ,
				}
				else {
					empresa = partes[4];
					p = new Convidado(email,nome,Integer.parseInt(idade),empresa);
					this.adicionar(p);
					if(partes.length>5)
						listaId = partes[5];		//lista de id separados por ,
				}

				if(!listaId.isEmpty()) {
					//relacionar participante com os eventos
					for(String idevento : listaId.split(",")){
						ev = this.localizarEvento(Integer.parseInt(idevento));
						ev.adicionar(p);
						p.adicionar(ev);
					}
				}
			}
			arquivo2.close();
		}
		catch(Exception ex)		{
			throw new RuntimeException("leitura arquivo de participantes:"+ex.getMessage());
		}
	}

	//--------------------------------------------------------------------
	public void	salvar()  {
		//gravar nos arquivos csv os objetos que estão no repositório
		FileWriter arquivo1=null;
		try	{
			File f = new File( new File(".\\eventos.csv").getCanonicalPath())  ;
			arquivo1 = new FileWriter(f); 
			for(Evento e : this.getEventos()) 	{
				arquivo1.write(e.getId()+";"+e.getData()+";"+e.getDescricao()+";"+e.getPreco()+"\n");	
			} 
			arquivo1.close();
		}
		catch(Exception e){
			throw new RuntimeException("problema na criação do arquivo  eventos "+e.getMessage());
		}

		FileWriter arquivo2=null;
		try	{
			File f = new File( new File(".\\participantes.csv").getCanonicalPath())  ;
			arquivo2 = new FileWriter(f) ; 
			ArrayList<String> lista ;
			String listaId;
			for(Participante p : this.getParticipantes()) {
				//montar uma lista com os id dos eventos do participante
				lista = new ArrayList<>();
				for(Evento e : p.getEventos()) {
					lista.add(e.getId()+"");
				}
				listaId = String.join(",", lista);

				if(p instanceof Convidado c )
					arquivo2.write("CONVIDADO;" +p.getEmail() +";" + p.getNome() +";" 
							+ p.getIdade() +";"+ c.getEmpresa() +";"+ listaId +"\n");
				else
					arquivo2.write("PARTICIPANTE;" +p.getEmail() +";" + p.getNome() +";" 
							+ p.getIdade() +";"+ listaId +"\n");	

			} 
			arquivo2.close();
		}
		catch (Exception e) {
			throw new RuntimeException("problema na criação do arquivo  participantes "+e.getMessage());
		}

	}
}

