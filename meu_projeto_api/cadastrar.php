<?php
// Usando a mesma conexão do seu arquivo de login
$host = 'www.thyagoquintas.com.br:3306';
$db   = 'engenharia_64';
$user = 'engenharia_64';
$pass = 'tamanduamirim';
$charset = 'utf8mb4';

$dsn = "mysql:host=$host;dbname=$db;charset=$charset";
$options = [
    PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
    PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
    PDO::ATTR_EMULATE_PREPARES   => false,
];

header('Content-Type: application/json');

// 1. Receber dados via POST, incluindo o CPF
$nome = $_POST['nome'] ?? '';
$email = $_POST['email'] ?? '';
$senha = $_POST['senha'] ?? '';
$cpf = $_POST['cpf'] ?? ''; // <-- RECEBE O CPF

// 2. Validação simples, incluindo o CPF
if (empty($nome) || empty($email) || empty($senha) || empty($cpf)) {
    echo json_encode(['status' => 'error', 'message' => 'Todos os campos são obrigatórios.']);
    exit;
}

// Criptografar a senha
$senha_hash = password_hash($senha, PASSWORD_DEFAULT);

try {
    $pdo = new PDO($dsn, $user, $pass, $options);

    // 3. Inserir o novo usuário no banco de dados, incluindo o CPF
    $sql = "INSERT INTO USUARIO (USUARIO_NOME, USUARIO_EMAIL, USUARIO_SENHA, USUARIO_CPF) VALUES (:nome, :email, :senha, :cpf)";
    $stmt = $pdo->prepare($sql);
    
    $stmt->execute([
        'nome' => $nome,
        'email' => $email,
        'senha' => $senha_hash,
        'cpf' => $cpf // <-- ADICIONA O CPF NA QUERY
    ]);

    // Retornar uma resposta de sucesso para o app
    echo json_encode(['status' => 'success', 'message' => 'Usuário cadastrado com sucesso!']);

} catch (\PDOException $e) {
    if ($e->getCode() == 23000) {
        echo json_encode(['status' => 'error', 'message' => 'Este e-mail ou CPF já está em uso.']);
    } else {
        echo json_encode(['status' => 'error', 'message' => 'Erro no banco de dados: ' . $e->getMessage()]);
    }
    exit;
}
?>
