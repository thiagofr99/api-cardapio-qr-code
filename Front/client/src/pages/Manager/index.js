import React,{useState, useEffect} from "react";
import { Link, useHistory} from "react-router-dom";

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faLinkedin, faGithub, faYoutube } from '@fortawesome/free-brands-svg-icons'

import api from '../../services/api'

import './style.css';

export default function Manager(){

    const [empresaResponse, setEmpresaResponse] = useState();
    const [empresas, setEmpresas] = useState([]);
    const [cardapios, setCardapios] = useState([]);

    console.log(cardapios);

    const accessToken = sessionStorage.getItem('accessToken');

    useEffect(()=> {
    
        api.get('api/empresa/v1/empresas-gerente', {
            headers:{
                Authorization: `Bearer ${accessToken}`
            }
        }).then(response => {
            setEmpresas(response.data)
        })
        
    },[]);

    return(
        <div id="container">
           
            <header>
                <nav>
                    <ul>
                        <li>
                            
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

            <select id="permissao" className="input-select" name="select" value={empresaResponse} onChange={e=> setEmpresaResponse(e.target.value)}>
                                <option value="">Selecione uma Empresa:</option>
                                {empresas.map( p=>(
                                          <option value={p.id}>{p.empresaNome}</option>                                     
                                ))}
                            
            </select>
            
            { cardapios.length == 0 ? ' ':
            <div id="lista-1">
                <table>
                    <tr>
                        <th>Nome do Cardápio</th>
                        <th>URL para Download</th>
                        <th>URL QR CODE</th>
                        <th>Ultima Atualização</th>
                        <th>Ações</th>
                    </tr>
                    
                    
                </table>
            </div>  
            }                                    
            

                

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