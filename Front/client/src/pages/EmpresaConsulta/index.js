import React,{useState, useEffect} from "react";
import { Link, useHistory, useParams} from "react-router-dom";
import InputMask from "react-input-mask";

import './style.css';


import api from '../../services/api'

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faLinkedin, faGithub, faYoutube } from '@fortawesome/free-brands-svg-icons'



export default function EmpresaConsulta(){


    const {nome} = useParams();

    const [empresaNome, setEmpresaNome] = useState('');
    const [cepMask, setCepMask] = useState('');
    const [numero, setNumero] = useState('');    
    const [complemento, setComplemento] = useState('');

    
    const [empresas, setEmpresas] = useState([]);  

    const accessToken = sessionStorage.getItem('accessToken');
    

    const history = useHistory();

    useEffect(()=> {
        findAllByEmpresaName();
    },[]);

    async function editEmpresa(id){
        try{
            sessionStorage.setItem('gerente', 'false');
            history.push(`/update/${id}`)
        } catch ( erro ){
            alert('Edit failed! Try again.')
        }
    }

    async function gerenteEmpresa(id){
        try{
            sessionStorage.setItem('gerente', 'true');
            history.push(`/update/${id}`)
        } catch ( erro ){
            alert('Edit failed! Try again.')
        }
    }


    async function findAllByEmpresaName(pagin){

        var paramers = new URLSearchParams();
        nome===undefined ? paramers.append("empresaName",''):paramers.append("empresaName",nome)

        try{
    
            const response = await api.get('api/empresa/v1/findAllByEmpresaName/',{                                
              headers:{
                  Authorization: `Bearer ${accessToken}`
              },
              params: {
                empresaName:  nome === undefined ? '' :  nome,
                page: pagin,
                limit: 10,
                direction: 'asc'
              }
            }).then(responses=> {
                setEmpresas(responses.data._embedded.empresaVoes)
            })
            
              
            alert('Busca realizada com sucesso.')          
      
          } catch (err){
            alert('Erro ao buscar registros!'+err)
          }
        

    }  

    async function deleteEmpresa(id) {

        var resultado = window.confirm("Deseja excluir o item selecionado?");

        if(resultado==true){
            try {
                await api.delete(`api/empresa/v1/${id}`, {
                    headers: {
                        Authorization: `Bearer ${accessToken}`
                    }
                })
                
                alert('Empresa deletada com sucesso!')
                setEmpresas(empresas.filter(emp => emp.id !== id))
            } catch (err) {
                alert('Delete failed! Try again.');
            }
        }
            
        
        
    }
    
    async function desabilitar(id){        
        
        var confirm = window.confirm("Deseja realmente desabilitar a empresa?")
        if(confirm){

        
            try{
                

                await api.patch(`/api/empresa/v1/desabilitar/${id}`, {
                    //dados que serão atualizados
                }, {
                    headers: {
                    'Authorization': `Bearer ${accessToken}`
                }
                })
                
                
                alert('Desabilitado com sucesso!')
                findAllByEmpresaName();          
        
            } catch (err){
                alert('Erro ao renovar registro!'+err)
            }
        }
    }  

    return (
        <div id="container">
           
            <header>
                <nav>
                    <ul>
                        <li>
                            <Link to="/usuario"> 
                                Usuarios
                            </Link>     
                            </li>
                            <li> <Link className="active" to="/empresa"> 
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
                        <th>Nome da Empresa</th>
                        <th>CEP</th>
                        <th>Numero</th>
                        <th>Complemento</th>
                        <th>Data Cadastro</th>                        
                        <th>Ações</th>
                    </tr>
                    {empresas.map( p=>(
                    <tr>
                        <td> {p.empresaNome} </td>
                        <td> {p.cep} </td>
                        <td> {p.numero} </td>
                        <td> {p.complemento} </td>
                        <td> {p.dataCadastro} </td>                        
                        <td>
                            <button onClick={()=> deleteEmpresa(p.id)} className="input-button-deletar" type="submit" >Deletar</button>
                            <button onClick={(()=> gerenteEmpresa(p.id))} className="input-button-patch" type="submit" >Gerente</button>
                            <button onClick={()=> editEmpresa(p.id)} className="input-button-alterar" type="submit" >Alterar</button>
                            <button onClick={()=> desabilitar(p.id)} className="input-button-patch" type="submit" >Desabilitar</button>
                        </td>
                    </tr>                 
                                ))}
                    
                </table>

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