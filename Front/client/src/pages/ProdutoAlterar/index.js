import React,{useState, useEffect} from "react";
import { Link, useHistory, useParams} from "react-router-dom";
import InputMask from "react-input-mask";

import './style.css';

import api from '../../services/api'

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faLinkedin, faGithub, faYoutube } from '@fortawesome/free-brands-svg-icons'



export default function ProdutoAlterar(){

    const {idProduto} = useParams();

    const [produtoNome, setProdutoNome] = useState('');
    const [valor, setValor] = useState('');
    const [cardapioId, setCardapioId] = useState('');    
    const [tipoProdutoVO, setTipoProdutoVO] = useState('');
    const [disponivel, setDisponivel] = useState('');
    const [atualizacao, setAtualizacao] = useState('');
    const [cadastro, setCadastro] = useState('');
    const [observacao, setObservacao] = useState('');

    const [tipo, setTipo] = useState([]);    

    const accessToken = sessionStorage.getItem('accessToken');    
    
    const history = useHistory();
    

    useEffect(()=> {
        findProdutoById();

        api.get(`api/produto/v1/tipo-produto`, {
            headers:{
                Authorization: `Bearer ${accessToken}`
            }
        }).then(response => {
            setTipo(response.data)
        })
       
    },[]);

    


    async function findProdutoById(){
        
        try{
    
            const response = await api.get(`api/produto/v1/${idProduto}`,{                                
                headers:{
                  Authorization: `Bearer ${accessToken}`
              }
            })
            
            let dataCadastro = response.data.dataCadastro === null || response.data.dataCadastro === '' ? '': response.data.dataCadastro.split("T", 10)[0];
            let dataAtualizacao = response.data.dataAtualizacao === null || response.data.dataAtualizacao === '' ? '': response.data.dataAtualizacao.split("T", 10)[0];
        
            setProdutoNome(response.data.produtoNome);
            setValor(response.data.valorProduto);            
            setTipoProdutoVO(response.data.tipoProdutoVO);      
            setObservacao(response.data.observacao);
            setCardapioId(response.data.cardapioId);
            setDisponivel(response.data.disponivel);
            setAtualizacao(dataAtualizacao);
            setCadastro(dataCadastro);           
            alert('Busca realizada com sucesso.')          
      
          } catch (err){
            alert('Erro ao buscar registros!'+err)
          }
        

    }  

    async function salvar(e){
        e.preventDefault();

        var confirm = window.confirm('Deseja salvar os novos dados?')

        if(confirm){            
        
            let valorProduto = valor.replace(",",".");
            var id = idProduto;

            const data = {
                id,
                valorProduto,
                tipoProdutoVO,
                produtoNome,
                observacao,
                cardapioId,
                disponivel
            }
        
            try{
        
            await api.put('api/produto/v1/atualizar',data,{
                headers:{
                    Authorization: `Bearer ${accessToken}`
                }
            });
                
            alert('Salvo com sucesso.')          
            setProdutoNome('');
            setValor('');
            setTipoProdutoVO('');
            setDisponivel('');
            setAtualizacao('');
            setCadastro('');

            history.push('/produto');
            } catch (err){
            alert('Erro ao salvar registro!')
            }
        }
        
    
    };

    return (
        <div id="container">
           
            <header>
                <nav>
                    <ul>
                        <li>                                
                            </li>
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
                    </div>
                    
                </nav>
            </header>
            <body>
                <h1>Cadastro de Produtos.</h1>          
                <div id="lista-1">                    
                    <div className="empresa-alterar">
                        <h3>Nome Produto:</h3>
                        <input type="text" value={produtoNome} className="" disabled={false} onChange={e=>setProdutoNome(e.target.value)} />
                        <h3>Valor Produto:</h3>
                        <input type="number" className={""} disabled={false}  value={valor} onChange={e=>setValor(e.target.value)} />
                        <h3>Observação:</h3>
                        <input type="text" className={""}  value={observacao} disabled={false} onChange={e=>setObservacao(e.target.value)} />
                        <h3>Tipo de Produto:</h3>
                        <select id="tipo" className="input-select-4" name="select" value={tipoProdutoVO} onChange={e=> setTipoProdutoVO(e.target.value)}>
                                <option value="">Selecione uma Opção!</option>
                                {tipo.map( p=>(
                                          <option selected={p.valorEnum===tipoProdutoVO ?"selected":""} value={p.valorEnum}>{p.descricao}</option>                                     
                                ))}
                            
                        </select>
                        
                        <h3>Data de Cadastro</h3>
                        <input type="date" className="disabled-input" disabled={true} value={cadastro} onChange={e=>setCadastro(e.target.value)}/>
                        <h3>Data de Atualização</h3>
                        <input type={atualizacao===null || atualizacao===''?"text":"date"} className="disabled-input" disabled={true} value={atualizacao} onChange={e=>setAtualizacao(e.target.value)}/>
                        <button onClick={salvar}>Salvar</button> 
                        
                    </div>

                    <div className="empresa-alterar-2">                        
                        
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
                        Copyright © www.devthiagofurtado.com 2022
                    </div> 
               
            </footer>
        </div>
        
    );
}