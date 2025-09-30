<?php
$host = 'www.thyagoquintas.com.br:3306';
$db   = 'engenharia_64';
$user = 'engenharia_64';
$pass = 'tamanduamirim';
$charset = 'utf8mb4';
$dsn  =  "mysql:host=$host;dbname=$db;charset=$charset";
$options = [
    PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
    PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
    PDO::ATTR_EMULATE_PREPARES   => false,
];

header('Content-Type: application/json');

try {
    $pdo = new PDO($dsn, $user, $pass, $options);
    $usuario_email = $_GET['usuario'] ?? '';
    $senha_digitada = $_GET['senha'] ?? '';

    if (empty($usuario_email) || empty($senha_digitada)) {
        echo json_encode([]); // Retorna array vazio se os campos estiverem vazios
        exit;
    }

    // 1. Busca o usuário APENAS pelo e-mail
    $sql = "SELECT USUARIO_ID as usuarioId,
                   USUARIO_NOME as usuarioNome, 
                   USUARIO_EMAIL as usuarioEmail, 
                   USUARIO_CPF as usuarioCpf,
                   USUARIO_SENHA as senhaHash 
            FROM USUARIO
            WHERE USUARIO_EMAIL = :usuario";

    $stmt = $pdo->prepare($sql);
    $stmt->execute(['usuario' => $usuario_email]);
    $usuario = $stmt->fetch(); // Pega apenas um usuário

    $response = []; // Prepara uma resposta vazia por padrão

    // 2. Se encontrou um usuário, verifica a senha
    if ($usuario) {
        // 3. Compara a senha digitada com a senha criptografada do banco
        if (password_verify($senha_digitada, $usuario['senhaHash'])) {
            // Senha correta! Prepara os dados para enviar de volta (sem a senha)
            unset($usuario['senhaHash']); // Remove a senha do array de resposta
            $response[] = $usuario; // Adiciona o usuário ao array de resposta
        }
    }

    // 4. Retorna a resposta (ou um array vazio se o login falhar)
    echo json_encode($response);

} catch (\PDOException $e) {
    // Em caso de erro de conexão, retorna um array vazio também
    echo json_encode([]);
    exit;
}
?>
