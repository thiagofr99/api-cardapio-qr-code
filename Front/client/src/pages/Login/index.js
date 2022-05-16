import React, {useState} from 'react';
import { useHistory} from 'react-router-dom';

import './style.css';

import perfil from '../../assets/perfil.png'

import logo from '../../assets/Logo Transparente.png'

import api from '../../services/api'

export default function Login() {

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const history = useHistory();

  async function login(e){
    e.preventDefault();

    const data = {
        username,
        password,
    }

    try{

      const response = await api.post('auth/signin',data);

      sessionStorage.setItem('username', username);
      sessionStorage.setItem('accessToken',response.data.token)
      sessionStorage.setItem('permission',response.data.permission.authority)

      if(response.data.permission.authority==="ADMIN"){
        history.push('/usuario')
      } 
      if(response.data.permission.authority==="MANAGER"){
        history.push('/manager')
      } 

    } catch (err){
      alert('Login failed! Try agains!')
    }

  };

    return (
    <div className="container">
        <section className='form'>
        <img className='perfil' src={perfil} alt=""/>
        <form onSubmit={login}>
          <h1>Acesse sua conta.</h1>
          <input className="email" type="text" name="userName" id="userName" placeholder="Digite o seu nome de usuário." value={username} onChange={e => setUsername(e.target.value)}/>
          <input className="senha" type="password" name="senha" id="senha" placeholder="Digite sua senha" value={password} onChange={e => setPassword(e.target.value)}/>
          <input className="submit" type="submit" value="Logar"/>
          <img className='logoTipo' src={logo} alt="" />
        </form>
        </section>
    </div>
    
    
    );
  }
  
  