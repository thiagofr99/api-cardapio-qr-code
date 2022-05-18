import React from 'react';
import {BrowserRouter, Route, Switch} from 'react-router-dom';

import Login from './pages/Login';
import Empresa from './pages/Empresa';
import Usuario from './pages/Usuario';
import Manager from './pages/Manager';
import UsuarioConsulta from './pages/UsuarioConsulta';
import UsuarioTodos from './pages/UsuarioTodos';
import EmpresaTodos from './pages/EmpresaTodos';


export default function Routes() {
    return (
        <BrowserRouter>
            <Switch>
                <Route path="/" exact component={Login}/>
                <Route path="/empresa" exact component={Empresa}/>                
                <Route path="/usuario" exact component={Usuario}/>                
                <Route path="/manager" component={Manager}/>                
                <Route path='/consulta/:nome' component={UsuarioConsulta}/>                
                <Route path='/todos/' component={UsuarioTodos}/>                
                <Route path='/busca/' exact component={EmpresaTodos}/>                
            </Switch>
        </BrowserRouter>
    );
}