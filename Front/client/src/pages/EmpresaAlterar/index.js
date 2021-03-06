import React,{useState, useEffect} from "react";
import { Link, useHistory, useParams} from "react-router-dom";
import InputMask from "react-input-mask";

import './style.css';

import imgEmpresa from '../../assets/default.jpg'

import api from '../../services/api'

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faLinkedin, faGithub, faYoutube } from '@fortawesome/free-brands-svg-icons'



export default function EmpresaAlterar(){

    const {myId} = useParams();

    const [empresaNome, setEmpresaNome] = useState('');
    const [cepMask, setCepMask] = useState('');
    const [numero, setNumero] = useState('');    
    const [complemento, setComplemento] = useState('');
    const [imageUrl, setImageUrl] = useState('');
    const [atualizacao, setAtualizacao] = useState('');
    const [cadastro, setCadastro] = useState('');
    const [userFullName, setUserFullName] = useState('');

    const [manangers, setManangers] = useState(['']);
    const [mananger, setMananger] = useState('');

    
    const [empresa, setEmpresa] = useState();  

    const accessToken = sessionStorage.getItem('accessToken');
    const gerente = sessionStorage.getItem('gerente');
    
    const history = useHistory();
    

    useEffect(()=> {
        findByEmpresaId();
        if(gerente==='true')
        findAllManangers();
    },[]);

    async function findAllManangers(){
        api.get('auth/manangers', {
            headers:{
                Authorization: `Bearer ${accessToken}`
            }
        }).then(response => {
            setManangers(response.data)
        })
    }


    async function findByEmpresaId(){
        
        try{
    
            const response = await api.get(`api/empresa/v1/${myId}`,{                                
                headers:{
                  Authorization: `Bearer ${accessToken}`
              }
            })
            
            let dataCadastro = response.data.dataCadastro === null || response.data.dataCadastro === '' ? '': response.data.dataCadastro.split("T", 10)[0];
            let dataAtualizacao = response.data.dataAtualizacao === null || response.data.dataAtualizacao === '' ? '': response.data.dataAtualizacao.split("T", 10)[0];
        
            setEmpresaNome(response.data.empresaNome);
            setCepMask(response.data.cep);
            setNumero(response.data.numero);
            setComplemento(response.data.complemento);
            setImageUrl(response.data.imageUrl);
            setAtualizacao(dataAtualizacao);
            setCadastro(dataCadastro);
            response.data.user===null || response.data.user===''? setUserFullName(''): setUserFullName(response.data.user.fullName);
            alert('Busca realizada com sucesso.')          
      
          } catch (err){
            alert('Erro ao buscar registros!'+err)
          }
        

    }  

    async function salvar(e){
        e.preventDefault();

        var confirm = window.confirm('Deseja salvar os novos dados?')

        if(confirm){
            var cep = cepMask.replace('-','');
        
            var id = myId;

            const data = {
                id,
                empresaNome,
                cep,
                complemento,
                numero
            }
        
            try{
        
            await api.put('api/empresa/v1/atualizar',data,{
                headers:{
                    Authorization: `Bearer ${accessToken}`
                }
            });
                
            alert('Salvo com sucesso.')          
            setEmpresaNome('');
            setCepMask('');
            setComplemento('');
            setNumero('');
            setAtualizacao('');
            setCadastro('');

            history.push('/empresa');
            } catch (err){
            alert('Erro ao salvar registro!')
            }
        }
        
    
    };

    async function definirGerente(userGerente, idEmpresa){        
        
        var confirm = window.confirm("Deseja definir "+userGerente+" como gerente da empresa?");
        if(confirm){
            try{
            

                await api.patch(`/api/empresa/v1/${idEmpresa}/gerente/${userGerente}`, {
                    //dados que ser??o atualizados
                  }, {
                    headers: {
                      'Authorization': `Bearer ${accessToken}`
                   }
                  })
                
                  
                alert('Gerente definido com sucesso!')
                history.push('/empresa');
          
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
                <h1>Cadastro de Empresas.</h1>          
                <div id="lista-1">                    
                    <div className="empresa-alterar">
                        <h3>Nome empresa:</h3>
                        <input type="text" value={empresaNome} className="disabled-input" disabled={true} onChange={e=>setEmpresaNome(e.target.value)} />
                        <h3>Cep:</h3>
                        <InputMask type="text" className={gerente==='true' ?"disabled-input":""} disabled={gerente==='true' ?true:false}  mask="99999-999" value={cepMask} onChange={e=>setCepMask(e.target.value)} />
                        <h3>Numero:</h3>
                        <input type="text" className={gerente==='true' ?"disabled-input":""}  value={numero} disabled={gerente==='true' ?true:false} onChange={e=>setNumero(e.target.value)} />
                        <h3>Complemento:</h3>
                        <input type="text" className={gerente==='true' ?"disabled-input":""} value={complemento} disabled={gerente==='true' ?true:false} onChange={e=>setComplemento(e.target.value)} />
                        <h3>Data de Cadastro</h3>
                        <input type="date" className="disabled-input" disabled={true} value={cadastro} onChange={e=>setCadastro(e.target.value)}/>
                        <h3>Data de Atualiza????o</h3>
                        <input type={atualizacao===null || atualizacao===''?"text":"date"} className="disabled-input" disabled={true} value={atualizacao} onChange={e=>setAtualizacao(e.target.value)}/>
                        {gerente === 'true' ? '': <button onClick={salvar}>Salvar</button>} 
                        
                    </div>

                    <div className="empresa-alterar-2">
                        <img className="image-empresa" src={imageUrl===null || imageUrl==='' ? imgEmpresa : imageUrl} alt="" />                        
                        {gerente==='true' && userFullName==='' ?                            
                            <select id="permissao" className="input-select" name="select" value={mananger} onChange={e=> setMananger(e.target.value)}>
                            <option value="">Selecione um gerente!</option>
                            {manangers.map( p=>(
                                    <option value={p.userName}>{p.fullName}</option>                                     
                            ))}
                        
                            </select>                            
                            : gerente==='false' ? '' : <input type="text" className={gerente==='true' ?"disabled-input":""}  value={"Gerente: "+userFullName} disabled={gerente==='true' ?true:false} onChange={e=>setUserFullName(e.target.value)} />    
                        }
                        {gerente==='true' && userFullName==='' ? <button onClick={()=> definirGerente(mananger,myId)}>Definir</button>: ''}                           
                    </div>
                    
                    <div className="clear"></div>
                    

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
                        Copyright ?? www.devthiagofurtado.com 2022
                    </div> 
               
            </footer>
        </div>
        
    );
}