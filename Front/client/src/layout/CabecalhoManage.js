import React from "react";
import { Link, useHistory} from "react-router-dom";

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faLinkedin } from '@fortawesome/free-brands-svg-icons'



export default function CabechalhoManage(){
    
    const usuarioLogado = sessionStorage.getItem('username');    
    const cargo = sessionStorage.getItem('permission'); 
    
    const history = useHistory();

    async function logout(){
        sessionStorage.setItem('username', '');
        sessionStorage.setItem('accessToken', '');
        sessionStorage.setItem('permission', '');
        history.push(`/`);        
    }

    return (
        <div>
            <header>
                <nav>
                    <ul>                    
                        <li> <Link className="active" to="/manager"> 
                                Empresas
                            </Link>     
                        </li>                        
                    </ul>                
                    <div id="cabecalho" className="flex">
                        <a className="linkedin-cab" href="https://www.linkedin.com/in/dev-thiago-furtado/">
                            <FontAwesomeIcon icon={faLinkedin} className="linkedin" />
                            <h2>@DEVTHIAGOFURTADO</h2>
                        </a>       
                        <h3 className="texto-bemvindo">Seja bem vindo {usuarioLogado+" - "+cargo}</h3>     
                        <button className="button-sair" onClick={logout}> Sair </button>            
                    </div>
                    
                </nav>
            </header>
        </div>
    );
}