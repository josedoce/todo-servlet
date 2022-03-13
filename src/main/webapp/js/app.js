/**
 * @author josedoce
 */
//base url da api
const BASE_URL = "http://localhost:8081";

function handleTodoList(){
	const todoList = document.getElementById("todo-list");
	axios.get(`${BASE_URL}/todo`)
	.then((res)=>{
		todoList.innerHTML = null;
		res.data.forEach((e)=>{
			todoList.innerHTML += `
				<li><span>${e.name}</span><div><button onclick="handleEditTodoForm(${e.id},'${e.name}')" class="edite custom--outline">editar</button> <button onclick="handleSubmitDelete(${e.id})" class="delete custom--outline">deletar</button></div></li>
			`;
		});
		
	}).catch((err)=>{
		console.log(err);
	})
}handleTodoList();

function handleSubmitDelete(id) {
	const userConfirm = confirm("Deseja excluir ?");
	if(!userConfirm) {
		return;
	}
	axios.delete(`${BASE_URL}/todo/${id}`)
	.then((res)=>{
		handleTodoList();
	}).catch((err)=>{
		console.log(err);
	})
}

const updateInputId = document.getElementById("update-todo-id");
const updateInputTodo = document.getElementById("update-todo");
function handleSubmitUpdate(event) {
	event.preventDefault();
	event.stopPropagation();
	const data = {
		name: updateInputTodo.value
	}
	axios.put(`${BASE_URL}/todo/${updateInputId.value}`, data)
	.then((res)=>{
		handleTodoList();
		handleEditTodoFormCancel();
	}).catch((err)=>{
		console.log(err);
	})
}
const createForm = document.getElementById("create-form");
const updateForm = document.getElementById("update-form");
const displayTodo = document.getElementById("todo-display");
function handleEditTodoForm(id, name) {
	updateInputId.value = id;
	updateInputTodo.value = name;
	createForm.style.display='none';
	displayTodo.style.display='none';
	updateForm.style.display='';
}
function handleEditTodoFormCancel() {
	createForm.style.display='';
	displayTodo.style.display='';
	updateForm.style.display='none';
}

function handleSubmitCreate(event){
	event.preventDefault();
	event.stopPropagation();
	const data = {
		name: document.getElementById("create-todo").value
	}
	axios.post(`${BASE_URL}/todo`, data)
	.then((res)=>{
		handleTodoList();
	}).catch((err)=>{
		console.log(err);
	})
}