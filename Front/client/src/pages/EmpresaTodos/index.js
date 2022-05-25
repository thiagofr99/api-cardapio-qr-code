import React,{useState, useEffect} from "react";
import { useHistory} from "react-router-dom";

import './style.css';

import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

import api from '../../services/api'

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faGithub, faYoutube } from '@fortawesome/free-brands-svg-icons'

import Loading from "../../layout/Loading";
import CabechalhoEmpresa from "../../layout/CabecalhoEmpresa";



export default function EmpresaTodos(){
    
    const [empresas, setEmpresas] = useState([]);  

    const accessToken = sessionStorage.getItem('accessToken'); 
    
    const [loadOn, setLoadOn] = useState(false);

    const history = useHistory();

    useEffect(()=> {
        findAllByEmpresaName();
    },[]);

    async function editEmpresa(id){
        try{
            sessionStorage.setItem('gerente', 'false');
            history.push(`/update/${id}`)
        } catch ( erro ){
            toast.error('Erro ao editar empresa.', {
                position: toast.POSITION.TOP_CENTER
              })
              setLoadOn(false);
        }
    }

    async function gerenteEmpresa(id){
        try{
            sessionStorage.setItem('gerente', 'true');
            history.push(`/update/${id}`)
        } catch ( erro ){
            toast.error('Erro ao acessar opção de gerentes.', {
                position: toast.POSITION.TOP_CENTER
              })
              setLoadOn(false);
        }
    }

    async function findAllByEmpresaName(){
        setLoadOn(true);

        try{

            await api.get('api/empresa/v1/findAllByEmpresaName/?page=0&limit=10&ASC',{                
              headers:{
                  Authorization: `Bearer ${accessToken}`
              }
            }).then(responses=> {
                setEmpresas(responses.data._embedded.empresaVoes)
            })
            
            toast.success('Busca realizada com sucesso.', {
                position: toast.POSITION.TOP_CENTER
              })
              setLoadOn(false);      

          } catch (err){
            toast.error('Erro ao buscar empresas.', {
                position: toast.POSITION.TOP_CENTER
              })
              setLoadOn(false);
          }
        

    }  

    async function deleteEmpresa(id) {
        setLoadOn(true);

        var resultado = window.confirm("Deseja excluir o item selecionado?");

        if(resultado==true){
            try {
                await api.delete(`api/empresa/v1/${id}`, {
                    headers: {
                        Authorization: `Bearer ${accessToken}`
                    }
                })
                
                toast.success('Empresa deletada com sucesso.', {
                    position: toast.POSITION.TOP_CENTER
                  })
                  setLoadOn(false);
                setEmpresas(empresas.filter(emp => emp.id !== id))
            } catch (err) {
                toast.error('Erro ao deletar empresa.', {
                    position: toast.POSITION.TOP_CENTER
                  })
                  setLoadOn(false);
            }
        }
            
        
        
    }
    
    async function desabilitar(id){
        
        var confirm = window.confirm("Deseja realmente desabilitar a empresa?")
        if(confirm){
        setLoadOn(true)
            try{

                await api.patch(`/api/empresa/v1/desabilitar/${id}`, {
                    //dados que serão atualizados
                }, {
                    headers: {
                    'Authorization': `Bearer ${accessToken}`
                }
                })
                
                
                toast.success('Empresa desabilitada com sucesso.', {
                    position: toast.POSITION.TOP_CENTER
                  })
                  setLoadOn(false);
                findAllByEmpresaName();          
        
            } catch (err){
                toast.error('Erro ao desabilitar empresa.', {
                    position: toast.POSITION.TOP_CENTER
                  })
                  setLoadOn(false);
            }
        }
    }  

    return (
        <div id="container">
           {loadOn? <Loading></Loading>:
            <div>
            <CabechalhoEmpresa></CabechalhoEmpresa>
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
}
        </div>
        
    );
}