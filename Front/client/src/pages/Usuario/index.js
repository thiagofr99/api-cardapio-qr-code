import React,{useState, useEffect} from "react";
import { Link, useHistory} from "react-router-dom";

import './style.css';

import api from '../../services/api'

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faLinkedin, faGithub, faYoutube } from '@fortawesome/free-brands-svg-icons'



export default function Usuario(){

    const [permissionsReturn, setPermissionsReturn] = useState([]);
    const [permissionsResponse, setPermissionsResponse] =useState([]);

    const [userName, setUsername] = useState('');
    const [fullName, setFullName] = useState('');
    const [password, setPassword] = useState('');    

    
    const [users, setUsers] = useState([]);

    
    const [userParams, setUserParams] =  useState('');    
    
    
    const accessToken = sessionStorage.getItem('accessToken');
    

    const history = useHistory();

    useEffect(()=> {
    
        api.get('auth/permissions', {
            headers:{
                Authorization: `Bearer ${accessToken}`
            }
        }).then(response => {
            setPermissionsReturn(response.data)
        })

    
        
    },[]);

    async function salvar(e){
        e.preventDefault();

        var permissions = [permissionsResponse];
    
        const data = {
            userName,
            fullName,
            password,
            permissions
        }
    
        try{
    
          const response = await api.post('auth/salvar',data,{
            headers:{
                Authorization: `Bearer ${accessToken}`
            }
          });
            
          alert('Salvo com sucesso.')

        setFullName('');
        setPassword('');
        setUsername('');
        setPermissionsResponse([]);          
    
        } catch (err){
          alert('Erro ao salvar registro!')
        }
    
      };

    async function findAllByUserName(e){
        e.preventDefault();

        var paramers = new URLSearchParams();
        paramers.append("userName", userParams);
        //paramers.append("page", 0);
        //paramers.append("limit", 5);
        //paramers.append("direction", 'ASC');


        try{
    
            const response = await api.get('auth/findAllByUserName/?page=0&limit=5&direction=ASC',{
                params: paramers,  
              headers:{
                  Authorization: `Bearer ${accessToken}`
              }
            }).then(responses=> {
                setUsers(responses.data._embedded.usuarioVoes)
            })
                        
              
            alert('Busca realizada com sucesso.')          
            
      
          } catch (err){
            alert('Erro ao buscar registros!'+err)
          }
        

    }  
    
    async function renovar(id){        
        
        try{
            
            await api.patch(`auth/${id}`,{
                headers:{
                    Authorization: `Bearer ${accessToken}`
                }
            }
            )
            
              
            alert('Renovado com sucesso!')          
      
          } catch (err){
            alert('Erro ao renovar registro!'+err)
          }
        

    }  

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
                <div id="consulta-1">
                    <div className="row-1">
                        <h2 className="text-consulta">CONSULTA TODOS OS USUÁRIO POR NOME.</h2>
                        
                    </div>
                    <div className="row-2">
                        <form className="consulta-1" onSubmit={findAllByUserName}>
                            <input className="input-1" type="text" name="id" id="" value={userParams} onChange={e => setUserParams(e.target.value)} placeholder="Digíte o nome ou parte do nome do Usuário."/>
                            <input className="input-2" type="submit" value="Consultar" />
                        </form>
                    </div>
                </div>

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
                    <tr>
                        <td> {p.userName} </td>
                        <td> {p.fullName} </td>
                        <td>{p.permissions.at(0).descricao}</td>
                        <td>{ p.dateLicense===null ? 'Licença Permanente': p.dateLicense }</td>
                        <td><button onClick={()=> renovar(p.id)} className="input-button-3" type="submit" >Renovar</button></td>
                    </tr>                 
                                ))}
                    
                </table>

                </div>

                <div id="consulta-2">
                    <div className="row-1">
                        <h2 className="text-consulta">SALVA UM NOVO USUÁRIO.</h2>
                        
                    </div>
                    <div className="row-2">
                        <form className="consulta-1" onSubmit={salvar}>
                            <input className="input-3" type="text" name="username" value={userName} onChange={e => setUsername(e.target.value)} placeholder="Login Usuário."/>
                            <input className="input-3" type="text" name="fullName" value={fullName} onChange={e => setFullName(e.target.value)} placeholder="Nome Completo."/>
                            <input className="input-3" type="password" name="password" value={password} onChange={e => setPassword(e.target.value)} placeholder="Password."/>
                            <select id="permissao" className="input-select" name="select" value={permissionsResponse} onChange={e=> setPermissionsResponse(e.target.value)}>
                                <option value="">Selecione uma Opção!</option>
                                {permissionsReturn.map( p=>(
                                          <option value={p.valorEnum}>{p.descricao}</option>                                     
                                ))}
                            
                            </select>
                            <input className="input-2" type="submit" value="Salvar" />
                        </form>
                    </div>
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