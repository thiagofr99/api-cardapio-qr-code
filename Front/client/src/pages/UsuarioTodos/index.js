import React,{useState, useEffect} from "react";
import { Link, useHistory} from "react-router-dom";

import './style.css';

import api from '../../services/api'

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faLinkedin, faGithub, faYoutube } from '@fortawesome/free-brands-svg-icons'



export default function UsuarioTodos(){
    
    
    const [users, setUsers] = useState([]);

    const [page, setPage] = useState(0);
    
    const accessToken = sessionStorage.getItem('accessToken');

    const history = useHistory();

    

    async function proximaPage(){
        var pagina=0;    
        page === 0? pagina=1: pagina=page;
        pagina = page > 0? pagina+1 : pagina;
        setPage(pagina);
        buscarTodosPorNome(pagina);        
    }

    async function anteriorPage(){
        var newPage;
        page > 0 ? newPage=page: alert('erro');  
        newPage = newPage-1;      
        setPage(newPage);
        buscarTodosPorNome(newPage);        
    }


    async function buscarTodosPorNome(pagin){
    
        try{            
            const response = await api.get('auth/findAllByUserName',{                
              headers:{
                  Authorization: `Bearer ${accessToken}`
              },
              params: {
                page: pagin,
                limit: 10,
                direction: 'asc'
              }
            }).then(responses=> {
                setUsers(responses.data._embedded.usuarioVoes)
            })
        
            console.log(accessToken);
              
            alert('Busca realizada com sucesso.')          
            
      
          } catch (err){
            alert('Erro ao buscar registros!'+err)
          }
        

    }  
    
    async function renovar(id){        
        
        console.log(accessToken);

        try{
            

            await api.patch(`/auth/${id}`, {
                //dados que serão atualizados
              }, {
                headers: {
                  'Authorization': `Bearer ${accessToken}`
               }
              })
            
              
            alert('Renovado com sucesso!')          
      
          } catch (err){
            alert('Erro ao renovar registro!'+err)
          }
        

    }  

 
    useEffect(()=> {
        try{            
            buscarTodosPorNome();
        } catch (erro){
            alert("erro:"+erro)
        }
        
        
    },[]);

    return (
        <div id="container">
           
            <header>
                <nav>
                    <ul>
                        <li>
                            <Link className="active" to="/usuario"> 
                                Usuarios
                            </Link>     
                            </li>
                            <li> <Link to="/empresa"> 
                                Empresas
                            </Link>     
                        </li>                        
                    </ul>                
                    <div id="cabecalho" className="flex">
                        <a className="linkedin-cab" href="https://www.linkedin.com/in/dev-thiago-furtado/">
                            <FontAwesomeIcon icon={faLinkedin} className="linkedin" />
                            <h2>@DEVTHIAGOFURTADO</h2>
                        </a>                        
                    </div>
                    
                </nav>
            </header>
            <body>                

                <div id="lista-1">
                <table>
                    <tr>
                        <th>Nome de Usuário</th>
                        <th>Nome completo</th>
                        <th>Cargo</th>
                        <th>Data Licença</th>
                        <th>Ações</th>
                    </tr>
                    {users.map( p=>(
                    <tr key={p.id}>
                        <td> {p.userName} </td>
                        <td> {p.fullName} </td>
                        <td>{p.permissions.at(0).descricao}</td>
                        <td>{ p.dateLicense===null || p.dateLicense==='' ? 'Licença Permanente': p.dateLicense }</td>
                        <td>{ p.dateLicense===null || p.dateLicense==='' || new Date(p.dateLicense) >= Date.now() ? '':<button onClick={()=> renovar(p.id)} className="input-button-3" type="submit" >Renovar</button>} </td>
                       
                    </tr>
                                ))}
                    
                </table>
                </div>
                <div className="nav-page">
                        {page===0 ? '' : <button className="button-previous" onClick={anteriorPage}>{'<<Anterior'}</button>}      
                        <h3>{page+1}</h3> 
                        { users.length < 9 ? '': <button className="button-next" onClick={proximaPage}>{'Próxima>>'}</button>} 
                </div>
            </body>
            <footer>
                <div className="dados-pessoais">
                    <div className="endereco">
                        <h2> Granja Portugal </h2>
                        <h3> Fortaleza - CE</h3>  
                    </div>
                    <div className="rede-social">                        
                        <a className="link-href" href="https://github.com/thiagofr99"><FontAwesomeIcon icon={faGithub} className="github" /><h3>/thiagofr99</h3></a> 
                        <a className="link-href-yt" href="https://www.youtube.com/channel/UCxxFrDeO_yXxRe7EB5aTjfA"><FontAwesomeIcon icon={faYoutube} className="youtube" /><h3>Thiago Furtado</h3></a> 
                    </div>
                                   
                </div>
                <div className="copyright">
                        Copyright © www.devthiagofurtado.com 2022
                    </div> 
               
            </footer>
        </div>
        
    );
}