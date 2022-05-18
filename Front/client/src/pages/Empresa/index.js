import React,{useState, useEffect} from "react";
import { Link, useHistory} from "react-router-dom";
import InputMask from "react-input-mask";

import './style.css';

import api from '../../services/api'

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faLinkedin, faGithub, faYoutube } from '@fortawesome/free-brands-svg-icons'



export default function Empresa(){

    const [permissionsReturn, setPermissionsReturn] = useState([]);
    const [permissionsResponse, setPermissionsResponse] =useState([]);

    const [empresaNome, setEmpresaNome] = useState('');
    const [cepMask, setCepMask] = useState('');
    const [numero, setNumero] = useState('');    
    const [complemento, setComplemento] = useState('');

    
    const [empresas, setEmpresas] = useState([]);

    
    const [empresaParams, setEmpresaParams] =  useState('');        

    const accessToken = sessionStorage.getItem('accessToken');
    

    const history = useHistory();

    useEffect(()=> {
    
    },[]);

    async function salvar(e){
        e.preventDefault();

        var cep = cepMask.replace('-','');
    
        const data = {
            empresaNome,
            cep,
            complemento,
            numero
        }
    
        try{
    
          await api.post('api/empresa/v1/salvar',data,{
            headers:{
                Authorization: `Bearer ${accessToken}`
            }
          });
            
          alert('Salvo com sucesso.')          
          setEmpresaNome('');
          setCepMask('');
          setComplemento('')
          setNumero('');
        } catch (err){
          alert('Erro ao salvar registro!')
        }
    
      };

    async function buscarEmpresas(nome){
        
        if(nome === undefined || nome===''){
            history.push(`/busca/`);
        } else {
            history.push(`/consulta/${nome}`);
        }

    }  
    
    async function renovar(id){        
        
        try{
            
            await api.put(`auth/${id}`,{
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
                <div id="consulta-1">
                    <div className="row-1">
                        <h2 className="text-consulta">CONSULTA TODAS AS EMPRESAS POR NOME.</h2>
                        
                    </div>
                    <div className="row-2">
                        <form className="consulta-1" onSubmit={()=>buscarEmpresas(empresaParams)}>
                            <input className="input-1" type="text" name="id" id="" value={empresaParams} onChange={e => setEmpresaParams(e.target.value)} placeholder="Digíte o nome ou parte do nome da empresa."/>
                            <input className="input-2" type="submit" value="Consultar" />
                        </form>
                    </div>
                </div>

                <div id="lista-1">
                <table>
                    <tr>
                        <th>Nome da Empresa</th>
                        <th>CEP</th>
                        <th>Numero</th>
                        <th>Complemento</th>
                        <th>Data Cadastro</th>                        
                    </tr>
                    {empresas.map( p=>(
                    <tr>
                        <td> {p.empresaNome} </td>
                        <td> {p.cep} </td>
                        <td> {p.numero} </td>
                        <td> {p.complemento} </td>
                        <td> {p.dataCadastro} </td>                        
                    </tr>                 
                                ))}
                    
                </table>

                </div>

                <div id="consulta-2">
                    <div className="row-1">
                        <h2 className="text-consulta">SALVA UMA NOVA EMPRESA.</h2>
                        
                    </div>
                    <div className="row-2">
                        <form className="consulta-1" onSubmit={salvar}>
                            <input className="input-3" type="text" name="nomeEmpresa" value={empresaNome} onChange={e => setEmpresaNome(e.target.value)} placeholder="Empresa nome."/>
                            <InputMask className="input-3" mask="99999-999" value={cepMask} onChange={e => setCepMask(e.target.value)} placeholder="CEP da empresa." />
                            <input className="input-3" type="number" name="numero" value={numero} onChange={e => setNumero(e.target.value)} placeholder="Numero."/>
                            <input className="input-3" type="text" name="complemento" value={complemento} onChange={e => setComplemento(e.target.value)} placeholder="Complemento."/>                            
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