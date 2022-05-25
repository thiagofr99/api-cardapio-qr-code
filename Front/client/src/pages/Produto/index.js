import React,{useState, useEffect} from "react";
import { Link, useHistory, useParams} from "react-router-dom";
import FileDownload from "js-file-download";

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faLinkedin, faGithub, faYoutube } from '@fortawesome/free-brands-svg-icons'

import api from '../../services/api'

import './style.css';

export default function Produto(){

    const {cdpId} = useParams();

    const [produtos, setProdutos] = useState([]);
    const [tipo, setTipo] = useState([]);
    const [tipoResponse, setTipoResponse] = useState();
    const [cardapios, setCardapios] = useState([]);
    const [observacao, setObservacao] = useState('');
    
    const[produtoNome, setProdutoNome] = useState('');
    const[valor, setValor] = useState('');

    const history = useHistory();

    const accessToken = sessionStorage.getItem('accessToken');

    useEffect(()=> {
    
        buscarPorCardapio();
        
        api.get(`api/produto/v1/tipo-produto`, {
            headers:{
                Authorization: `Bearer ${accessToken}`
            }
        }).then(response => {
            setTipo(response.data)
        })
        
    },[]);

    async function alterar(id){
        history.push(`/editar/${id}`);
    }

    async function buscarPorCardapio(){
        api.get(`api/produto/v1/findAllByCardapio/${cdpId}`, {
            headers:{
                Authorization: `Bearer ${accessToken}`
            }
        }).then(response => {
            setProdutos(response.data)
        })

    }

    async function excluir(id) {

        var resultado = window.confirm("Deseja excluir o item selecionado?");

        if(resultado==true){
            try {
                await api.delete(`api/produto/v1/${id}`, {
                    headers: {
                        Authorization: `Bearer ${accessToken}`
                    }
                })
                
                alert('Produto deletado com sucesso!')
                setProdutos(cardapios.filter(c => c.id !== id))
            } catch (err) {
                alert('Delete failed! Try again.');
            }
        }
            
        
        
    }    

    async function alternarDisponivel(id) {

        var resultado = window.confirm("Deseja alterar disponibilidade do item selecionado?");

        if(resultado==true){
            try {
                await api.patch(`api/produto/v1/${id}`,
                {},
                {    
                    headers: {
                        Authorization: `Bearer ${accessToken}`
                    }
                })
                alert("Alterado com sucesso!");
                buscarPorCardapio();
            } catch (err) {
                alert('Falha ao alterar.');
            }
        }
            
        
        
    }    

    async function salvar(e) {
        e.preventDefault();

        var cardapioId = cdpId;
        var tipoProdutoVO = tipoResponse;
        var valorProduto = valor.replace(",",".");

        const data = {
            cardapioId,
            observacao,
            produtoNome,
            tipoProdutoVO,
            valorProduto,
        }

        try{
            await api.post('api/produto/v1/salvar',data,{
                headers:{
                    Authorization: `Bearer ${accessToken}`
                }
              })
            
            setObservacao('');
            setProdutoNome('');
            setValor('');
            setTipoResponse('');
    
            alert("Produto salvo com sucesso.")
            buscarPorCardapio();
        } catch( erro ){
            alert("Erro ao salvar Produto "+ erro)
        }

        
    }

    return(
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

            <div id="consulta-2">
                    <div className="row-1">
                        <h2 className="text-consulta">SALVA UM NOVO PRODUTO.</h2>
                        
                    </div>
                    <div className="row-2">
                        <form className="consulta-1" onSubmit={salvar}>
                            <input className="input-3" type="text" name="produtoNome" value={produtoNome} onChange={e => setProdutoNome(e.target.value)} placeholder="Produto nome."/>
                            <input className="input-3" type="text" name="valorProduto" value={valor} onChange={e => setValor(e.target.value)} placeholder="Valor produto."/>
                            <input className="input-3" type="text" name="observacao" value={observacao} onChange={e => setObservacao(e.target.value)} placeholder="Observação."/>
                            <select id="tipo" className="input-select-3" name="select" value={tipoResponse} onChange={e=> setTipoResponse(e.target.value)}>
                                <option value="">Selecione uma Opção!</option>
                                {tipo.map( p=>(
                                          <option value={p.valorEnum}>{p.descricao}</option>                                     
                                ))}
                            
                            </select>
                            <input className="input-2" type="submit" value="Salvar" />
                        </form>
                    </div>
            </div>            
            
            <div id="lista-1">

                <table>
                    <tr>
                        <th>Nome do Produto</th>
                        <th>Valor Produto</th>
                        <th>Observação</th>
                        <th>Ultima Atualização</th>
                        <th>Ações</th>
                    </tr>
{produtos.map( p=>(                    

                            <tr key={p.id}>
                                <td> {p.produtoNome} </td>
                                <td> {Intl.NumberFormat('pt-BR', {style: 'currency', currency: 'BRL'}).format(p.valorProduto)} </td>
                                <td> {p.observacao} </td>
                                <td>{ p.dataAtualizacao ===null || p.dataAtualizacao ==='' ? Intl.DateTimeFormat('pt-BR').format(new Date(p.dataCadastro)):Intl.DateTimeFormat('pt-BR').format(new Date(p.dataAtualizacao))}</td>
                                <td>{ p.disponivel===true ? <button className="input-button-patch" onClick={()=> alternarDisponivel(p.id)} >Disponível</button> 
                                                            : <button className="input-button-deletar" onClick={()=> alternarDisponivel(p.id)} >Indisponível</button>}
                                    { p.disponivel===true ? <button className="input-button-alterar" onClick={()=> alterar(p.id)} >Alterar</button> :" "}
                                    <button className="input-button-deletar" onClick={()=> excluir(p.id)} >Excluir</button>
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